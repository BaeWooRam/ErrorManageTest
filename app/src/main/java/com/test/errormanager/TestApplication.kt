package com.test.errormanager

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import com.test.errormanager.integration.single.ErrorActivity
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.system.exitProcess


class TestApplication : Application() {
    private val TAG = javaClass.simpleName
    private val activityStack = Stack<Activity>()

    override fun onCreate() {
        super.onCreate()
        setSentry()
        setCrashHandler()
    }

    private fun setSentry(){
        SentryAndroid.init(this) { options ->
            options.setBeforeViewHierarchyCaptureCallback { event, hint, debounce ->
                Log.d(TAG, "options > BeforeViewHierarchyCaptureCallback event = $event")
                Log.d(TAG, "options > BeforeViewHierarchyCaptureCallback hint = $hint")
                Log.d(TAG, "options > BeforeViewHierarchyCaptureCallback debounce = $debounce")
                // always capture crashed events
                if (event.isCrashed) {
                    return@setBeforeViewHierarchyCaptureCallback true
                }

                // if debounce is active, skip capturing
                if (debounce) {
                    return@setBeforeViewHierarchyCaptureCallback false
                } else {
                    // also capture fatal events
                    return@setBeforeViewHierarchyCaptureCallback event.level == SentryLevel.FATAL
                }
            }

            options.isEnableUserInteractionTracing = true
            options.isEnableUserInteractionBreadcrumbs = true
        }
    }

    private fun setCrashHandler() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityStack.push(activity)
                Log.d(TAG, "onActivityCreated activityStack = $activityStack")
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                activityStack.remove(activity)
                Log.d(TAG, "onActivityDestroyed activityStack = $activityStack")
            }
        })

        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            Log.d(TAG, "setDefaultUncaughtExceptionHandler thread = $thread, exception = $exception")
            val stringWriter = StringWriter()
            exception.printStackTrace(PrintWriter(stringWriter))
            Log.d(TAG, "setDefaultUncaughtExceptionHandler stringWriter = ${stringWriter.toString()}")

            val lastActivity = activityStack.lastElement()
            Log.d(TAG, "setDefaultUncaughtExceptionHandler lastActivity = $lastActivity")

            startErrorActivity(lastActivity, stringWriter.toString())
            Process.killProcess(Process.myPid())
            exitProcess(-1)
        }
    }

    private fun startErrorActivity(activity: Activity, errorText: String) = activity.run {
        val errorActivityIntent = Intent(this, ErrorActivity::class.java).apply {
            putExtra(ErrorActivity.EXTRA_INTENT, activity.intent)
            putExtra(ErrorActivity.EXTRA_ACTIVITY_CLASS, activity.javaClass)
            putExtra(ErrorActivity.EXTRA_ERROR_TEXT, errorText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        Log.d(TAG, "setDefaultUncaughtExceptionHandler startErrorActivity errorActivityIntent = $errorActivityIntent")
        startActivity(errorActivityIntent)
        finish()
    }
}
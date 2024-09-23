package com.test.errormanager.crashlytics

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.test.errormanager.Error
import com.test.errormanager.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.random.Random


class CrashlyticsCustomizeActivity : AppCompatActivity() {
    private val crashlytics = Firebase.crashlytics
    private val TAG = "CustomizeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crashlytics_customize)

        findViewById<Button>(R.id.CustomKeyButton).setOnClickListener {
            settingCustomKey()
            Error.executeException()
        }

        findViewById<Button>(R.id.CustomShortLogButton).setOnClickListener {
            settingCustomLog(true)
            Error.executeException()
        }

        findViewById<Button>(R.id.CustomLongLogButton).setOnClickListener {
            settingCustomLog(false)
            Error.executeException()
        }

        findViewById<Button>(R.id.CustomUserIdButton).setOnClickListener {
            settingUserId()
            Error.executeException()
        }

        findViewById<Button>(R.id.CustomKeyButton).setOnClickListener {
            recordException()
        }
    }

    private fun settingCustomKey() {
        val randomSeed = Random.nextInt()
        crashlytics.setCustomKey("CustomKey", "Test $randomSeed")
    }

    private fun settingCustomLog(isShort: Boolean) {
        if (isShort)
            crashlytics.log("Test Short Log")
        else {
            val readFile = readErrorFile()
            Log.d(TAG, "settingCustomLog readFile = $readFile")
            crashlytics.log(readFile)
        }
    }

    private fun readErrorFile(): String {
        val readStr = StringBuilder()
        val assetManager = resources.assets
        val inputStream: InputStream = assetManager.open("error.txt")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        bufferedReader.use {
            var str: String? = null
            while (it.readLine()?.also { line -> str = line } != null) {
                readStr.append(str)
            }
        }
        return readStr.toString()
    }

    private fun settingUserId() {
        val randomSeed = Random.nextInt()
        crashlytics.setUserId("Test User Id $randomSeed")
    }

    private fun recordException() {
        kotlin.runCatching {
            Error.executeException()
        }.onFailure { e ->
            Log.d(TAG, "recordException e = $e")
            crashlytics.recordException(e)
        }
    }

}
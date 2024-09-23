package com.test.errormanager.sentry

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.test.errormanager.Error
import com.test.errormanager.R
import io.sentry.Sentry
import kotlin.random.Random

class SentryActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sentry)

        findViewById<Button>(R.id.ErrorButton).setOnClickListener {
            Error.executeException()
        }

        findViewById<Button>(R.id.RecordExceptionButton).setOnClickListener {
            recordException()
        }
    }

    private fun recordException() {
        kotlin.runCatching {
            Error.executeException()
        }.onFailure { e ->
            Log.d(TAG, "recordException e = $e")
            Sentry.captureException(e)
        }
    }
}
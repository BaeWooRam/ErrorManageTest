package com.test.errormanager.integration.single

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.test.errormanager.R

class ErrorActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private val lastActivityIntent by lazy { intent.getParcelableExtra<Intent>(EXTRA_INTENT) }
    private val lastActivityClass by lazy { intent.getSerializableExtra(EXTRA_ACTIVITY_CLASS) as Class<*>}
    private val errorText by lazy { intent.getStringExtra(EXTRA_ERROR_TEXT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        Log.d(TAG, "onCreate errorText = $errorText, lastActivityIntent = $lastActivityIntent")

        findViewById<TextView>(R.id.tv_error_log).text = errorText
        findViewById<Button>(R.id.btn_reload).setOnClickListener {
            kotlin.runCatching {
                startActivity(Intent(this@ErrorActivity, lastActivityClass).apply {
                    intent = lastActivityIntent
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }.onSuccess {
                finish()
            }.onFailure { e ->
                Log.d(TAG, "runCatching onFailure e = $e")
            }
        }
    }


    companion object {
        const val EXTRA_INTENT = "EXTRA_INTENT"
        const val EXTRA_ACTIVITY_CLASS = "EXTRA_ACTIVITY_CLASS"
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }
}
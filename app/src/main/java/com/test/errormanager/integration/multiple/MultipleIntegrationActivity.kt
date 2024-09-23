package com.test.errormanager.integration.multiple

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.test.errormanager.R
import com.test.errormanager.Error

class MultipleIntegrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integration)

        findViewById<TextView>(R.id.tvActivityName).apply {
            setOnClickListener {
                //Test1Activity Launch
                startActivity(Intent(this@MultipleIntegrationActivity, Test1Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })

                //Test2Activity Launch
                startActivity(Intent(this@MultipleIntegrationActivity, Test2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
            text = javaClass.simpleName
        }

        findViewById<Button>(R.id.ErrorButton).setOnClickListener {
            Error.executeException()
        }
    }

}

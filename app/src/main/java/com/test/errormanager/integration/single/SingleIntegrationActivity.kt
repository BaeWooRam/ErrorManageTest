package com.test.errormanager.integration.single

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.test.errormanager.Error
import com.test.errormanager.R

class SingleIntegrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integration)

        findViewById<TextView>(R.id.tvActivityName).text = javaClass.simpleName
        findViewById<Button>(R.id.ErrorButton).setOnClickListener {
            Error.executeException()
        }
    }

}
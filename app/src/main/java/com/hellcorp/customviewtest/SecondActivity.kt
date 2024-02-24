package com.hellcorp.customviewtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        findViewById<Button>(R.id.top_button).text = "Top button - Second Activity"
        findViewById<Button>(R.id.bottom_button).text = "Bottom button - Second Activity"
    }
}
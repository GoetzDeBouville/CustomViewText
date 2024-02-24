package com.hellcorp.customviewtest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.top_button).setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }
        var currentProgress = 0f
        val progressView = findViewById<CircularProgressView>(R.id.customProgressView)
//        lifecycleScope.launch {
//            repeat(100) {
//                currentProgress += getRandomFloatInRange(10f)
//                progressView.setCurrentProgress(currentProgress)
//                delay(500L)
//            }
//        }
    }

    private fun getRandomFloatInRange(max: Float): Float {
        return Random.nextFloat() * max
    }
}
package com.example.jesusdefeatingsatan

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.delay
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    var singer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        singer = MediaPlayer.create(this, R.raw.i_wanna_follow_jesus)
        if (singer != null) {
            singer!!.start()
        }
        if (singer != null) {
            singer!!.setOnCompletionListener {
                finish()
            }
        }
        setContentView(R.layout.activity_main)
        val myMessage = findViewById<TextView>(R.id.aarons_message)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            myMessage.visibility = VISIBLE
            Timer().schedule(1000) {
                myMessage.visibility = INVISIBLE
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun showText() {

    }
}
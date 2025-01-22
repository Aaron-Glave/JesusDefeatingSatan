package com.example.jesusdefeatingsatan

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    val listener = AppLifecycleListener()
    override fun onCreate(savedInstanceState: Bundle?) {
        listener.installMedia(MediaPlayer.create(baseContext, R.raw.i_wanna_follow_jesus))
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*if (singer != null) {
            singer!!.start()
        }*/

        /*singer.setOnCompletionListener {
            //finish()
        }
         */
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
    override fun onPause() {
        super.onPause()
        listener.onStop(this)
    }

    override fun onResume() {
        super.onResume()
        listener.onResume(this)
    }

    override fun onStart() {
        super.onStart()
        listener.onStart(this)
    }

    override fun onStop() {
        super.onStop()
        listener.onStop(this)
    }
}

class AppLifecycleListener : DefaultLifecycleObserver {
    var mediaPlayer: MediaPlayer? = null

    fun installMedia(externplayer: MediaPlayer) {
        mediaPlayer = externplayer
        mediaPlayer!!.start()
        mediaPlayer!!.isLooping = true
        playing = true
    }
    private var playing = false

    override fun onStart(owner: LifecycleOwner) { // app moved to foreground
        if (mediaPlayer != null && !playing) {
            mediaPlayer!!.start()
            playing = true
        }
    }

    override fun onStop(owner: LifecycleOwner) { // app moved to background
        if (mediaPlayer != null && playing) {
            mediaPlayer!!.pause()
            playing = false
        }
    }
}
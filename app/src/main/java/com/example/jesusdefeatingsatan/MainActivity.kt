package com.example.jesusdefeatingsatan

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
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
import androidx.core.view.isInvisible

interface Ender {
    fun endApp()
}
class MainActivity : AppCompatActivity(), Ender {
    var listener: AppLifecycleListener? = null
    //val listener = AppLifecycleListener(baseContext, this)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val myMessage = findViewById<TextView>(R.id.aarons_message)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            if(myMessage.isInvisible) {
                Log.i("press.button", "Pressed while invisible.")
                listener?.onUsefulTap()
                myMessage.visibility = VISIBLE
                Timer().schedule(1000) {
                    myMessage.visibility = INVISIBLE
                }
            } else {
                Log.i("press.button", "Pressed while already visible.")
            }
        }
        listener = AppLifecycleListener(context = baseContext, ender = this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onPause() {
        super.onPause()
        Log.d("c_time", "pause?")
        listener?.onPause(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d("c_time", "resume?")
        listener?.onResume(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d("c_time", "start?")
        if (listener?.needsStart() == true) {
            listener?.startPlaying()
        }
        listener?.onStart(this)
    }

    override fun onStop() {
        Log.d("c_time", "stop?")
        listener?.onStop(this)
        super.onStop()
    }

    override fun endApp() {
        Log.i("play.song", "Don't wanna listen anymore")
        finishAndRemoveTask()
    }
}

class AppLifecycleListener(val context: Context, val ender: Ender) : DefaultLifecycleObserver {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var hasStarted = false

    var timesPressed: Int = 0

    fun startPlaying() {
        mediaPlayer = MediaPlayer.create(context, R.raw.i_wanna_follow_jesus)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            playSongIfTapped()
        }
    }

    fun onUsefulTap() {
        timesPressed += 1
        Log.d("press.button", "Current count: $timesPressed")
    }

    fun playSongIfTapped() {
        if (timesPressed >= 5) {
            Log.i("c_time", "restarted")
            timesPressed = 0
            mediaPlayer = MediaPlayer.create(context, R.raw.i_wanna_follow_jesus)
            mediaPlayer?.setOnCompletionListener {
                playSongIfTapped()
            }
            mediaPlayer?.start()
        } else {
            Log.d("ENDING", "ENDING")
            ender.endApp()
        }
    }

    override fun onStart(owner: LifecycleOwner) { // app moved to foreground
        if (!hasStarted) {
            if (mediaPlayer != null) {
                mediaPlayer!!.start()
            }
            startPlaying()
            hasStarted = true
        } else {
            if (mediaPlayer != null) {
                val currentSpot = mediaPlayer?.currentPosition
                Log.d("c_time","Current spot: ${mediaPlayer?.currentPosition}")
                if(currentSpot != null) {
                    Log.d("c_time","Resumed")
                    mediaPlayer?.seekTo(currentSpot)
                    mediaPlayer?.start()
                } else {
                    Log.d("c_time", "Oh no! Didn't save where we ended...")
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) { // app moved to background
        if (mediaPlayer != null) {
            mediaPlayer!!.pause()
            currentPosition = mediaPlayer!!.currentPosition
            Log.d("c_time", "Current positon: $currentPosition")
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (mediaPlayer != null) {
            mediaPlayer!!.seekTo(currentPosition)
        }
    }

    fun needsStart(): Boolean {
        return !hasStarted
    }

}
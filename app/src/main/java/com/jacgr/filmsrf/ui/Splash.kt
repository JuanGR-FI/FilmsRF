package com.jacgr.filmsrf.ui

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jacgr.filmsrf.R
import kotlin.concurrent.thread

class Splash : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        thread {
            //Reproduccion de un audio local
            mp = MediaPlayer.create(this, R.raw.popcorn_notification)
            mp.start()
            Thread.sleep(1500)
            startActivity(Intent(this, Login::class.java))
            finish()
        }

    }
}
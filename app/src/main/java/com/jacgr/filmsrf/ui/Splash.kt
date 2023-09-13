package com.jacgr.filmsrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jacgr.filmsrf.R
import kotlin.concurrent.thread

class Splash : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        thread {
            Thread.sleep(1500)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}
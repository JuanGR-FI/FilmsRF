package com.jacgr.filmsrf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jacgr.filmsrf.R
import com.jacgr.filmsrf.databinding.ActivityMainBinding
import com.jacgr.filmsrf.ui.fragments.FilmsListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FilmsListFragment())
                .commit()
        }


    }
}
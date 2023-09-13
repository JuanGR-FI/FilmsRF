package com.jacgr.filmsrf.application

import android.app.Application
import com.jacgr.filmsrf.data.FilmRepository
import com.jacgr.filmsrf.data.remote.RetrofitHelper

class FilmRFApp: Application() {

    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        FilmRepository(retrofit)
    }

}
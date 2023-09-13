package com.jacgr.filmsrf.data

import com.jacgr.filmsrf.data.remote.FilmsApi
import com.jacgr.filmsrf.data.remote.model.FilmDto
import retrofit2.Call
import retrofit2.Retrofit

class FilmRepository(private val retrofit: Retrofit) {

    private val filmsApi: FilmsApi = retrofit.create(FilmsApi::class.java)

    fun getFilms(): Call<List<FilmDto>> = filmsApi.getFilms()

}
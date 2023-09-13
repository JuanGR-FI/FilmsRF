package com.jacgr.filmsrf.data.remote

import com.jacgr.filmsrf.data.remote.model.FilmDto
import retrofit2.Call
import retrofit2.http.GET

interface FilmsApi {

    @GET("films/films_list")
    fun getFilms(): Call<List<FilmDto>>

}
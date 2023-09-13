package com.jacgr.filmsrf.data.remote

import com.jacgr.filmsrf.data.remote.model.FilmDetailDto
import com.jacgr.filmsrf.data.remote.model.FilmDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FilmsApi {

    @GET("films/films_list")
    fun getFilms(): Call<List<FilmDto>>

    @GET("films/film_detail/{id}")
    fun getFilmDetail(
        @Path("id") id: String?
    ): Call<FilmDetailDto>

}
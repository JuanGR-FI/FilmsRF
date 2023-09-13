package com.jacgr.filmsrf.data.remote.model

data class FilmDetailDto(
    var title: String? = null,
    var image: String? = null,
    var director: String? = null,
    var genre: String? = null,
    var year: String? = null,
    var stars: String? = null,
    var overview: String? = null
)
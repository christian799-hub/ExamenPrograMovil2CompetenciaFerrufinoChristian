package edu.ucb.cashin.movie.domain.model

// Este modelo es el que utiliza la aplicación, sin depender de Ktor ni del JSON.
data class MovieModel(
    val id: String,
    val title: String,
    val posterPath: String,
)

package edu.ucb.cashin.movie.data.dto

import kotlinx.serialization.Serializable

// Serializable permite que Ktor convierta automáticamente el JSON a esta clase.
@Serializable
data class CatalogDto(
    val page: Int,
    // "results" es el arreglo de películas que devuelve TMDB.
    val results: List<MovieDto> = emptyList(),
)

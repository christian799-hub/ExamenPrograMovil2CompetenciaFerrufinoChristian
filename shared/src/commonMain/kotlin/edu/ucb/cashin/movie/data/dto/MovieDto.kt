package edu.ucb.cashin.movie.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO significa Data Transfer Object: representa exactamente los datos del API.
@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    // SerialName relaciona "poster_path" del JSON con "posterPath" de Kotlin.
    @SerialName("poster_path")
    val posterPath: String? = null,
)

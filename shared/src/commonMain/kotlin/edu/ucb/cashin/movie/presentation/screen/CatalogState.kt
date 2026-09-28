package edu.ucb.cashin.movie.presentation.screen

import edu.ucb.cashin.movie.domain.model.MovieModel

// State contiene toda la información necesaria para dibujar la pantalla.
data class CatalogState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movies: List<MovieModel> = emptyList(),
)

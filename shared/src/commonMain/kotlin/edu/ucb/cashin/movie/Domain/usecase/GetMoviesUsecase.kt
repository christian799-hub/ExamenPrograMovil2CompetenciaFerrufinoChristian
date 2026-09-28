package edu.ucb.cashin.movie.domain.usecase

import edu.ucb.cashin.movie.domain.model.MovieModel
import edu.ucb.cashin.movie.domain.repository.CatalogRepository

// El caso de uso representa la acción de solicitar las películas de la cartelera.
class GetMoviesUsecase(
    private val repository: CatalogRepository,
) {
    suspend fun invoke(): Result<List<MovieModel>> = repository.getMovies()
}

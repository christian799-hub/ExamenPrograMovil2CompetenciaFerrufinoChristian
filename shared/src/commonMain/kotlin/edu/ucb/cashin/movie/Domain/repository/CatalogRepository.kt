package edu.ucb.cashin.movie.domain.repository

import edu.ucb.cashin.movie.domain.model.MovieModel

// Domain declara qué necesita, pero no sabe cómo se consulta el API.
interface CatalogRepository {
    suspend fun getMovies(): Result<List<MovieModel>>
}

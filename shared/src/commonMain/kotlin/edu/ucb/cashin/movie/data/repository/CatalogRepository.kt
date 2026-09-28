package edu.ucb.cashin.movie.data.repository

import edu.ucb.cashin.movie.data.datasource.CatalogRemoteDataSource
import edu.ucb.cashin.movie.data.mapper.toDomain
import edu.ucb.cashin.movie.domain.model.MovieModel
import edu.ucb.cashin.movie.domain.repository.CatalogRepository
import kotlinx.coroutines.CancellationException

// "Impl" indica que esta clase implementa el contrato declarado en domain.
class CatalogRepositoryImpl(
    private val dataSource: CatalogRemoteDataSource,
) : CatalogRepository {

    override suspend fun getMovies(): Result<List<MovieModel>> {
        return try {
            // La capa data obtiene DTOs y el mapper los convierte a modelos del dominio.
            val movies = dataSource.getMovies().map { movieDto -> movieDto.toDomain() }
            Result.success(movies)
        } catch (exception: CancellationException) {
            // Una cancelación de corrutina no es un error de red y debe continuar propagándose.
            throw exception
        } catch (exception: Exception) {
            // Los demás errores se entregan al ViewModel mediante Result.failure.
            Result.failure(exception)
        }
    }
}

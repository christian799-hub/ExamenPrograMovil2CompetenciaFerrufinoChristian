package edu.ucb.cashin.movie.data.datasource

import edu.ucb.cashin.movie.data.dto.MovieDto

// Este contrato permite cambiar el servicio remoto sin modificar el repositorio.
interface CatalogRemoteDataSource {
    // Una función suspend puede esperar la respuesta HTTP sin bloquear la interfaz.
    suspend fun getMovies(): List<MovieDto>
}

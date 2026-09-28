package edu.ucb.cashin.movie.data.service

import edu.ucb.cashin.movie.data.datasource.CatalogRemoteDataSource
import edu.ucb.cashin.movie.data.dto.CatalogDto
import edu.ucb.cashin.movie.data.dto.MovieDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// El service se encarga únicamente de comunicarse con el API remoto.
class CatalogService : CatalogRemoteDataSource {

    // HttpClient es el cliente multiplataforma usado para hacer solicitudes HTTP.
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    // TMDB devuelve más campos de los declarados en los DTO; se ignoran sin fallar.
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
        }
    }

    override suspend fun getMovies(): List<MovieDto> {
        // GET solicita la cartelera ordenada por popularidad.
        val response = client.get(MOVIES_URL)

        // body<CatalogDto>() deserializa el JSON y results contiene la lista buscada.
        return response.body<CatalogDto>().results
    }

    private companion object {
        // Se conserva la URL que ya utilizabas para hacer el cambio lo menos invasivo posible.
        const val MOVIES_URL =
            "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=fa3e844ce31744388e07fa47c7c5d8c3"
    }
}

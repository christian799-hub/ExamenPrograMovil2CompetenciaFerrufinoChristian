package edu.ucb.cashin.movie.data.mapper

import edu.ucb.cashin.movie.data.dto.MovieDto
import edu.ucb.cashin.movie.domain.model.MovieModel

// Esta función de extensión transforma la respuesta del API en un modelo de la aplicación.
fun MovieDto.toDomain(): MovieModel = MovieModel(
    id = id.toString(),
    title = title,
    // TMDB puede devolver poster_path como null; orEmpty evita enviar null al dominio.
    posterPath = posterPath.orEmpty(),
)

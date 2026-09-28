package edu.ucb.cashin.movie.presentation.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.cashin.movie.domain.usecase.GetMoviesUsecase
import edu.ucb.cashin.movie.presentation.screen.CatalogEffect
import edu.ucb.cashin.movie.presentation.screen.CatalogEvent
import edu.ucb.cashin.movie.presentation.screen.CatalogState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatalogViewModel(
    private val getMoviesUsecase: GetMoviesUsecase,
) : ViewModel() {

    // MutableStateFlow solo puede modificarse dentro del ViewModel.
    private val _state = MutableStateFlow(CatalogState())
    // La pantalla recibe una versión de solo lectura.
    val state = _state.asStateFlow()

    // Channel conserva los effects hasta que la pantalla pueda procesarlos.
    private val _effect = Channel<CatalogEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        // Carga la cartelera automáticamente al abrir la pantalla.
        emitEvent(CatalogEvent.LoadMovies)
    }

    fun emitEvent(event: CatalogEvent) {
        when (event) {
            CatalogEvent.LoadMovies -> loadMovies()
            is CatalogEvent.OnShowDetail -> {
                _effect.trySend(CatalogEffect.NavigateToDetail(event.movieId))
            }
        }
    }

    private fun loadMovies() {
        // viewModelScope cancela este trabajo cuando el ViewModel deja de existir.
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(isLoading = true, error = null)
            }

            getMoviesUsecase.invoke().fold(
                onSuccess = { movies ->
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, movies = movies)
                    }
                },
                onFailure = { error ->
                    val message = error.message ?: "No se pudo cargar la cartelera"
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, error = message)
                    }
                    _effect.send(CatalogEffect.ShowMessage(message))
                },
            )
        }
    }
}

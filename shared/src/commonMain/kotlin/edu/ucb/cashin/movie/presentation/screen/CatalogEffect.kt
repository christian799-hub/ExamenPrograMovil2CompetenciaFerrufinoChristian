package edu.ucb.cashin.movie.presentation.screen

// Los effects representan acciones que deben ocurrir una sola vez.
sealed interface CatalogEffect {
    data class ShowMessage(val message: String) : CatalogEffect
    data class NavigateToDetail(val movieId: String) : CatalogEffect
}

// Los events son las acciones que la pantalla envía al ViewModel.
sealed interface CatalogEvent {
    data object LoadMovies : CatalogEvent
    data class OnShowDetail(val movieId: String) : CatalogEvent
}

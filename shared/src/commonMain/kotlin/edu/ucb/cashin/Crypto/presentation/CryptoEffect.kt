package edu.ucb.cashin.Crypto.presentation

sealed interface CryptoEffect {
    data class ShowMessage(val message: String) : CryptoEffect
    data class NavigateToDetail(val cryptoId: String) : CryptoEffect
}

sealed interface CryptoEvent {
    data object LoadCryptos : CryptoEvent
    data class OnShowDetail(val cryptoId: String) : CryptoEvent
}
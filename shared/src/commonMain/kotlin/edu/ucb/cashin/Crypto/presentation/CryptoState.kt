package edu.ucb.cashin.Crypto.presentation

import edu.ucb.cashin.Crypto.domain.model.CryptoModel

data class CryptoState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cryptos: List<CryptoModel> = emptyList(),
)

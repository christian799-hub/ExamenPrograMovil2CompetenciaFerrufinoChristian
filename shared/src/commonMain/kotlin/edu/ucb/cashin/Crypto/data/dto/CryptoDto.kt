package edu.ucb.cashin.Crypto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CryptoDto(
    val id: String,
    val name: String,
    @SerialName("current_price")
    val currentPrice: Double,
)

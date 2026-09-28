package edu.ucb.cashin.Crypto.data.service

import edu.ucb.cashin.Crypto.data.datasource.CryptoRemoteDataSource
import edu.ucb.cashin.Crypto.data.dto.CryptoDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CryptoService : CryptoRemoteDataSource {
    private val client = HttpClient {
        install(plugin = ContentNegotiation){
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
        }
    }

    override suspend fun getCryptos(): List<CryptoDto> {
        val response = client.get("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd")
        return response.body<List<CryptoDto>>()
    }
}




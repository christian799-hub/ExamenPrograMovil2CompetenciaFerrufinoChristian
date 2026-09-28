package edu.ucb.cashin.Crypto.data.datasource

import edu.ucb.cashin.Crypto.data.dto.CryptoDto

interface CryptoRemoteDataSource {
    suspend fun getCryptos(): List<CryptoDto>
}





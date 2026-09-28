package edu.ucb.cashin.Crypto.domain.repository

import edu.ucb.cashin.Crypto.domain.model.CryptoModel

interface CryptoRepository {
    suspend fun getCrypto() : Result<List<CryptoModel>>
}
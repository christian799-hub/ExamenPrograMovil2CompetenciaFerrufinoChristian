package edu.ucb.cashin.Crypto.domain.usecase

import edu.ucb.cashin.Crypto.domain.model.CryptoModel
import edu.ucb.cashin.Crypto.domain.repository.CryptoRepository


class CryptoUseCase(
    private val repository: CryptoRepository
) {
    suspend fun invoke(): Result<List<CryptoModel>> = repository.getCrypto()
}
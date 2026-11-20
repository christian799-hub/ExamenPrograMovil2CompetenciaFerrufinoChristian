package edu.ucb.cashin.exchange.domain.usecase

import kotlinx.coroutines.flow.Flow
import edu.ucb.cashin.exchange.domain.repository.ExchangeRepository

class ObserveExchangeUseCase(
    val repository: ExchangeRepository
) {
    suspend fun invoke(): Flow<String?> {
        return repository.observe()
    }
}
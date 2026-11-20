package edu.ucb.cashin.exchange.domain.repository

import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    suspend fun observe(): Flow<String?>
}
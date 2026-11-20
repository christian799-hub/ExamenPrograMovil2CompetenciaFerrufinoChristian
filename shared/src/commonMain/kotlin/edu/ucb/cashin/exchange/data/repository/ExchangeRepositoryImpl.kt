package edu.ucb.cashin.exchange.data.repository

import edu.ucb.cashin.exchange.data.datasource.RealTimeDataBase
import edu.ucb.cashin.exchange.domain.repository.ExchangeRepository

import kotlinx.coroutines.flow.Flow

class ExchangeRepositoryImpl(
    val realTimeDataBase: RealTimeDataBase
): ExchangeRepository {
    override suspend fun observe(): Flow<String?> {
        return realTimeDataBase.observeMessage()
    }
}
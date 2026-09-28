package edu.ucb.cashin.Crypto.data.repository

import edu.ucb.cashin.Crypto.data.datasource.CryptoRemoteDataSource
import edu.ucb.cashin.Crypto.data.mapper.toDomain
import edu.ucb.cashin.Crypto.domain.model.CryptoModel
import edu.ucb.cashin.Crypto.domain.repository.CryptoRepository
import kotlinx.coroutines.CancellationException

class CryptoRepositoryImpl(
    private val dataSource: CryptoRemoteDataSource,
) : CryptoRepository {
    override suspend fun getCrypto(): Result<List<CryptoModel>> {
        return try {
            val crypto = dataSource.getCryptos().map { criptoDto -> criptoDto.toDomain() }
            Result.success(crypto)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
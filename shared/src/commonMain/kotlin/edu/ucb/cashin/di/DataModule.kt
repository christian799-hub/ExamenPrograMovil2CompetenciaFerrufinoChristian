package edu.ucb.cashin.di

import edu.ucb.cashin.Crypto.data.datasource.CryptoRemoteDataSource
import edu.ucb.cashin.Crypto.data.repository.CryptoRepositoryImpl
import edu.ucb.cashin.Crypto.data.service.CryptoService
import edu.ucb.cashin.Crypto.domain.repository.CryptoRepository as CryptoRepositoryContract
import edu.ucb.cashin.movie.data.datasource.CatalogRemoteDataSource
import edu.ucb.cashin.movie.data.repository.CatalogRepositoryImpl
import edu.ucb.cashin.movie.data.service.CatalogService
import edu.ucb.cashin.movie.domain.repository.CatalogRepository
import org.koin.dsl.module

val dataModule = module {
    // El service consulta TMDB y se registra mediante su interfaz.
    single<CatalogRemoteDataSource> { CatalogService() }
    // El repositorio convierte DTOs en modelos del dominio.
    single<CatalogRepository> { CatalogRepositoryImpl(get()) }
    single<CryptoRemoteDataSource> { CryptoService() }
    single<CryptoRepositoryContract> { CryptoRepositoryImpl(get()) }
}

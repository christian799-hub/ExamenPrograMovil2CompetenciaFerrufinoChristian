package edu.ucb.cashin.di

import edu.ucb.cashin.Crypto.domain.usecase.CryptoUseCase
import edu.ucb.cashin.movie.domain.usecase.GetMoviesUsecase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    // Koin crea el caso de uso y obtiene automáticamente el repositorio.
    factoryOf(::GetMoviesUsecase)
    factoryOf(::CryptoUseCase)
}

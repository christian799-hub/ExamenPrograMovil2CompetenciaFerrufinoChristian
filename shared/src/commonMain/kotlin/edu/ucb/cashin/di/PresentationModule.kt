package edu.ucb.cashin.di

import edu.ucb.cashin.Crypto.presentation.viewmodel.CryptoViewModel
import edu.ucb.cashin.movie.presentation.screen.viewmodel.CatalogViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    // El ViewModel recibe GetMoviesUsecase por inyección de dependencias.
    viewModelOf(::CatalogViewModel)
    viewModelOf(::CryptoViewModel)
}

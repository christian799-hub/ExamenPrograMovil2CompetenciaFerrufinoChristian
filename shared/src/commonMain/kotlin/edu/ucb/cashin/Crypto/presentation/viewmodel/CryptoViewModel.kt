package edu.ucb.cashin.Crypto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.cashin.Crypto.domain.usecase.CryptoUseCase
import edu.ucb.cashin.Crypto.presentation.CryptoEffect
import edu.ucb.cashin.Crypto.presentation.CryptoEvent
import edu.ucb.cashin.Crypto.presentation.CryptoState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val getCryptoUseCase: CryptoUseCase
) : ViewModel(){
    private val _state = MutableStateFlow(CryptoState())
    val state = _state.asStateFlow()

    private val _effect = Channel<CryptoEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        emmitEvent(CryptoEvent.LoadCryptos)
    }

    fun emmitEvent(event: CryptoEvent){
        when(event){
            CryptoEvent.LoadCryptos -> loadCryptos()
            is CryptoEvent.OnShowDetail -> {
                _effect.trySend(CryptoEffect.NavigateToDetail(event.cryptoId))
            }
        }
    }

    private fun loadCryptos() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(isLoading = true, error = null)
            }

            getCryptoUseCase.invoke().fold(
                onSuccess = { cryptos ->
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, cryptos = cryptos)
                    }
                },
                onFailure = { error ->
                    val message = error.message ?: "No se pudieron cargar las cryptos"
                    _state.update { currentState ->
                        currentState.copy(isLoading = false, error = message)
                    }
                    _effect.send(CryptoEffect.ShowMessage(message))
                },
            )
        }
    }
}

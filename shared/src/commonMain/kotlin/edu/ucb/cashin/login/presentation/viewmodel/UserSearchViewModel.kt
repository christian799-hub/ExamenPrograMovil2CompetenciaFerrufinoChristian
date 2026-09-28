package edu.ucb.cashin.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucb.cashin.login.domain.usecase.SearchUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSearchViewModel(val searchUseCase: SearchUserUseCase): ViewModel() {
    private val _state = MutableStateFlow(UserSearchState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<UserSearchEffect>()
    val effect = _effects.asSharedFlow()

    fun emitEvent(event: UserSearchEvent){
        when(event){
            is UserSearchEvent.onAliasChange -> {
                _state.update { it.copy(alias = event.value) }
            }
            is UserSearchEvent.onSubmit -> {
                // La búsqueda de usuario queda aislada y no interviene con cartelera.
            }
        }
    }
    private fun eventEffect(effect: UserSearchEffect){
        viewModelScope.launch{
            _effects.emit(effect)
        }
    }
}

package edu.ucb.cashin.login.presentation.viewmodel

sealed interface UserSearchEffect {
    data class ShowToast(val message: String): UserSearchEffect
}

sealed interface UserSearchEvent {
    data class onAliasChange(val value: String): UserSearchEvent
    object onSubmit: UserSearchEvent
}

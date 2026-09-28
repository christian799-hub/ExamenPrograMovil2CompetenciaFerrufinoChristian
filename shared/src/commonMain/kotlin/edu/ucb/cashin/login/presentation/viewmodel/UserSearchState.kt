package edu.ucb.cashin.login.presentation.viewmodel

data class UserSearchState(
    val alias: String = "",
    val error: String = "",
    val isLoading: Boolean = false
)

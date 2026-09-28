package edu.ucb.cashin.login.domain.model

data class UserInfoModel(
    val alias: String,
    val company: String,
    val email: String,
    val avatarUrl: String
)
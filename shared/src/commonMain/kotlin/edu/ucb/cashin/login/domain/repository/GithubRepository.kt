package edu.ucb.cashin.login.domain.repository

import edu.ucb.cashin.login.domain.model.UserInfoModel

interface GithubRepository {
    suspend fun findByUserId(userId: String): Result<UserInfoModel>
}
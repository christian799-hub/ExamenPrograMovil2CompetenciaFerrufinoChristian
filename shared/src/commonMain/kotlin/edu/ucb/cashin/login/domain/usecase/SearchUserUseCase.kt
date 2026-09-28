package edu.ucb.cashin.login.domain.usecase

import edu.ucb.cashin.login.domain.model.UserInfoModel
import edu.ucb.cashin.login.domain.repository.GithubRepository

class SearchUserUseCase(val repository: GithubRepository) {
    suspend fun invoke(userId: String): Result<UserInfoModel>{
        return  repository.findByUserId(userId)
    }
}

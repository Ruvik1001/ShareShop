package com.grishina.domain.auth.usecase

import com.grishina.domain.auth.AuthRepository
import com.grishina.domain.data.User

class GetUserUseCase(private val authRepository: AuthRepository) {
    fun execute(): User? {
        val fbUser = authRepository.getUser()
        return if (fbUser != null) User(fbUser.email!!) else null
    }
}
package com.grishina.domain.auth.usecase

import com.grishina.domain.auth.AuthRepository
import com.grishina.domain.data.User
import kotlinx.coroutines.tasks.await

class SignUpUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(userAuthData: User): Boolean {
        return try {
            val task = authRepository.signUp(userAuthData)
            task.await()
            true
        } catch (e: Exception) {
            false
        }
    }
}

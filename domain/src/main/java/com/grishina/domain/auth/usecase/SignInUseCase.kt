package com.grishina.domain.auth.usecase

import com.grishina.domain.auth.AuthRepository
import com.grishina.domain.data.User
import kotlinx.coroutines.tasks.await

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(userAuthData: User): User? {
        return try {
            val task = authRepository.signIn(userAuthData)
            task.await()
            User(
                login = userAuthData.login,
                password = ""
            )
        } catch (e: Exception) {
            null
        }
    }
}

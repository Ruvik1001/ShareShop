package com.grishina.domain.auth.usecase

import com.grishina.domain.auth.AuthRepository
import com.grishina.domain.data.User
import kotlinx.coroutines.tasks.await

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(user: User): User? {
        return try {
            val task = authRepository.signIn(user)
            task.await()
            User(
                login = user.login,
                password = ""
            )
        } catch (e: Exception) {
            null
        }
    }
}

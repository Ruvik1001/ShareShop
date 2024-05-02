package com.grishina.domain.auth.usecase

import com.grishina.domain.auth.AuthRepository
import kotlinx.coroutines.tasks.await

class ResetPasswordUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(email: String): Boolean {
        return try {
            val methods = authRepository.fetchSignInMethodsForEmail(email)

            if (methods.signInMethods?.isEmpty() == true) {
                false
            } else {
                val task = authRepository.resetPassword(email)
                task.await()
                true
            }
        } catch (e: Exception) {
            false
        }
    }
}

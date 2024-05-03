package com.grishina.domain.auth.usecase

import com.grishina.domain.auth.AuthRepository

class SignOutUseCase(private val authRepository: AuthRepository) {
    fun execute() {
        authRepository.signOut()
    }
}
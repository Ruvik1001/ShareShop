package com.grishina.domain.share.usecase

import com.grishina.domain.data.User
import com.grishina.domain.share.ShareRepository

class RegisterInRTDBUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(user: User, callback: (Boolean)->Unit) {
        return shareRepository.registerInRTDB(user, callback)
    }
}
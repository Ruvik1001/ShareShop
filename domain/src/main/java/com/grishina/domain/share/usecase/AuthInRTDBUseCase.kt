package com.grishina.domain.share.usecase

import com.grishina.domain.data.User
import com.grishina.domain.share.ShareRepository

class AuthInRTDBUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(login: String, callback: (Boolean, User?)->Unit) {
        return shareRepository.authInRTDB(User(login = login), callback)
    }
}
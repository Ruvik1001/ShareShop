package com.grishina.domain.share.usecase

import com.grishina.domain.data.User
import com.grishina.domain.share.ShareRepository

class GetUserByTokenUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(userToken: String, callback: (User?)->Unit) {
        shareRepository.getUserByToken(userToken, callback)
    }
}
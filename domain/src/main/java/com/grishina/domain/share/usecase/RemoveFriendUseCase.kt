package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class RemoveFriendUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(fromToken: String, toToken: String): Boolean {
        return shareRepository.removeFriend(fromToken, toToken)
    }
}

package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class SendFriendRequestUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(fromToken: String, toToken: String, successCallback: (Boolean)->Unit) {
        return shareRepository.sendFriendRequest(fromToken, toToken, successCallback)
    }
}

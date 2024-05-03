package com.grishina.domain.share.usecase

import com.grishina.domain.data.FriendRequest
import com.grishina.domain.share.ShareRepository

class RemoveFriendUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(request: FriendRequest, successCallback: (Boolean)->Unit) {
        return shareRepository.removeFriend(request, successCallback)
    }
}

package com.grishina.domain.share.usecase

import com.grishina.domain.data.FriendRequest
import com.grishina.domain.share.ShareRepository

class LoadFriendRequestsUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(myToken: String): List<FriendRequest> {
        return shareRepository.loadFriendRequests(myToken)
    }
}

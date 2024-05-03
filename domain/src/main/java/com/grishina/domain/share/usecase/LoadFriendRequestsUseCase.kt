package com.grishina.domain.share.usecase

import com.grishina.domain.data.FriendRequest
import com.grishina.domain.share.ShareRepository

class LoadFriendRequestsUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(myToken: String, callback: (Boolean, List<FriendRequest>?)->Unit) {
        return shareRepository.loadFriendRequests(myToken, callback)
    }
}

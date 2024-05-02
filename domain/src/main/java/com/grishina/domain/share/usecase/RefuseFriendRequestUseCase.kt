package com.grishina.domain.share.usecase

import com.grishina.domain.data.FriendRequest
import com.grishina.domain.share.ShareRepository

class RefuseFriendRequestUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(request: FriendRequest): Boolean {
        return shareRepository.refuseFriendRequest(request)
    }
}

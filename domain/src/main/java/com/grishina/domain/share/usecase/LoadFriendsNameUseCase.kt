package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class LoadFriendsNameUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(friendsTokens: List<String>): List<String> {
        return shareRepository.loadFriendsName(friendsTokens)
    }
}

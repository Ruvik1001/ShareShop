package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class LoadFriendsNameUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(friendsTokens: List<String>, callback: (Boolean, List<Pair<String, String>>?) -> Unit) {
        return shareRepository.loadFriendsName(friendsTokens, callback)
    }
}

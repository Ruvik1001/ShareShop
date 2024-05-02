package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class AddFriendToListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(ownerToken: String, title: String, friendToken: String): Boolean {
        return shareRepository.addFriendToList(ownerToken, title, friendToken)
    }
}

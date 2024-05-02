package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class RemoveFriendFromListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(ownerToken: String, title: String, friendToken: String): Boolean {
        return shareRepository.removeFriendFromList(ownerToken, title, friendToken)
    }
}

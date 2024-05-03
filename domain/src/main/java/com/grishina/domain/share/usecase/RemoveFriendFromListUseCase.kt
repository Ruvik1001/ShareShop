package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class RemoveFriendFromListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, friendToken: String, successCallback: (Boolean)->Unit) {
        return shareRepository.removeFriendFromList(listToken, friendToken, successCallback)
    }
}

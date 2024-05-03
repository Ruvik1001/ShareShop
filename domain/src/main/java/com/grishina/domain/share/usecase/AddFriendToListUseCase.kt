package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class AddFriendToListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, friendToken: String, successCallback: (Boolean)->Unit) {
        return shareRepository.addFriendToList(listToken, friendToken, successCallback)
    }
}

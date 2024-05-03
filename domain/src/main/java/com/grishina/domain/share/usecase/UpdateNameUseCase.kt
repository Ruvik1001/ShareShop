package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class UpdateNameUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(userToken: String, newName: String, callback: (Boolean)->Unit) {
        shareRepository.updateName(userToken, newName, callback)
    }
}
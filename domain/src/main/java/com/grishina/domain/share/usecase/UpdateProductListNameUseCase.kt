package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class UpdateProductListNameUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, newTitle: String, callback: (Boolean)->Unit) {
        shareRepository.updateProductListName(listToken, newTitle, callback)
    }
}
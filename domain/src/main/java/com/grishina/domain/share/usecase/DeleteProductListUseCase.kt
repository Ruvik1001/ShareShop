package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class DeleteProductListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, successCallback: (Boolean)->Unit) {
        return shareRepository.deleteProductList(listToken, successCallback)
    }
}

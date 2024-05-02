package com.grishina.domain.share.usecase

import com.grishina.domain.share.ShareRepository

class DeleteProductListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(ownerToken: String, title: String): Boolean {
        return shareRepository.deleteProductList(ownerToken, title)
    }
}

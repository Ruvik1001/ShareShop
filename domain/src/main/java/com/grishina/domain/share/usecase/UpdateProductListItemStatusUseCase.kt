package com.grishina.domain.share.usecase

import com.grishina.domain.data.ItemStatus
import com.grishina.domain.share.ShareRepository

class UpdateProductListItemStatusUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(ownerToken: String, title: String, itemName: String, status: ItemStatus): Boolean {
        return shareRepository.updateProductListItemStatus(ownerToken, title, itemName, status)
    }
}

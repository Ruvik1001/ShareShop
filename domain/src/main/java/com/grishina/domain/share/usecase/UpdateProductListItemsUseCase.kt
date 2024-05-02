package com.grishina.domain.share.usecase

import com.grishina.domain.data.ListItem
import com.grishina.domain.share.ShareRepository

class UpdateProductListItemsUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(ownerToken: String, title: String, items: List<ListItem>): Boolean {
        return shareRepository.updateProductListItems(ownerToken, title, items)
    }
}

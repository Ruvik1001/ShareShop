package com.grishina.domain.share.usecase

import com.grishina.domain.data.ListItem
import com.grishina.domain.share.ShareRepository

class UpdateProductListItemsUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, items: List<ListItem>, successCallback: (Boolean)->Unit) {
        return shareRepository.updateProductListItems(listToken, items, successCallback)
    }
}

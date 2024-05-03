package com.grishina.domain.share.usecase

import com.grishina.domain.data.ItemStatus
import com.grishina.domain.share.ShareRepository

class UpdateProductListItemStatusUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, itemIndex: Int, status: ItemStatus, successCallback: (Boolean)->Unit) {
        return shareRepository.updateProductListItemStatus(listToken, itemIndex, status, successCallback)
    }
}

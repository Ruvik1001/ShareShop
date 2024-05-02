package com.grishina.domain.share.usecase

import com.grishina.domain.data.ProductList
import com.grishina.domain.share.ShareRepository

class LoadProductListsUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(myToken: String): List<ProductList> {
        return shareRepository.loadProductLists(myToken)
    }
}

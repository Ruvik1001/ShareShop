package com.grishina.domain.share.usecase

import com.grishina.domain.data.ProductList
import com.grishina.domain.share.ShareRepository

class LoadProductListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(listToken: String, callback: (ProductList?)->Unit) {
        return shareRepository.loadProductList(listToken = listToken, callback)
    }
}

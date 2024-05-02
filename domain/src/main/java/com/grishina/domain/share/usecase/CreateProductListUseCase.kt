package com.grishina.domain.share.usecase

import com.grishina.domain.data.ProductList
import com.grishina.domain.share.ShareRepository

class CreateProductListUseCase(private val shareRepository: ShareRepository) {
    suspend fun execute(productList: ProductList): Boolean {
        return shareRepository.createProductList(productList)
    }
}
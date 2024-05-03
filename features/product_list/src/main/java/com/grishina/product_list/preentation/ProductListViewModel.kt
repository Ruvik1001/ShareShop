package com.grishina.product_list.preentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.domain.data.ProductList
import com.grishina.domain.share.usecase.LoadProductListUseCase
import com.grishina.domain.share.usecase.UpdateProductListItemStatusUseCase
import com.grishina.domain.share.usecase.UpdateProductListItemsUseCase
import com.grishina.product_list.ProductListRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productListRouter: ProductListRouter,
    private val loadProductListUseCase: LoadProductListUseCase,
    private val updateProductListItemsUseCase: UpdateProductListItemsUseCase,
    private val updateProductListItemStatusUseCase: UpdateProductListItemStatusUseCase
) : ViewModel() {
    private val mList = MutableLiveData<ProductList>()
    val list: LiveData<ProductList> = mList

    fun loadList(listToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            loadProductListUseCase.execute(listToken) {
                mList.postValue(it)
            }
        }
    }

    fun updateProductList(products: List<ListItem>, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            mList.value?.let { updateProductListItemsUseCase.execute(it.listToken, products, callback) }
        }
    }

    fun updateProductList(itemIndex: Int, status: ItemStatus, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            mList.value?.let { updateProductListItemStatusUseCase.execute(it.listToken, itemIndex, status, callback) }
        }
    }

    fun lunchProductListSettings(listToken: String) {
        productListRouter.goToProductListSettings(listToken)
    }

}
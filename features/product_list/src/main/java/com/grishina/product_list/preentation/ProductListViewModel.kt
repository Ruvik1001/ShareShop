package com.grishina.product_list.preentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.domain.data.ProductList
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.LoadProductListUseCase
import com.grishina.domain.share.usecase.ObserveListChangesUseCase
import com.grishina.domain.share.usecase.UpdateProductListItemStatusUseCase
import com.grishina.domain.share.usecase.UpdateProductListItemsUseCase
import com.grishina.domain.share.usecase.UpdateProductListNameUseCase
import com.grishina.product_list.ProductListRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productListRouter: ProductListRouter,
    private val getUserUseCase: GetUserUseCase,
    private val loadProductListUseCase: LoadProductListUseCase,
    private val observeListChangesUseCase: ObserveListChangesUseCase,
    private val updateProductListNameUseCase: UpdateProductListNameUseCase,
    private val updateProductListItemsUseCase: UpdateProductListItemsUseCase,
    private val updateProductListItemStatusUseCase: UpdateProductListItemStatusUseCase,
) : ViewModel() {
    private val mList = MutableLiveData<ProductList>()
    val list: LiveData<ProductList> = mList

    private val user: User = getUserUseCase.execute()!!

    fun getUser(): User = user

    fun loadList(listToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            loadProductListUseCase.execute(listToken) {
                mList.postValue(it)
            }
        }
    }

    fun observeListChanges(listToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            observeListChangesUseCase.execute(listToken) { productList ->
                productList?.let {
                    CoroutineScope(Dispatchers.Main).launch {
                        mList.postValue(productList)
                    }
                }
            }
        }
    }

    fun updateProductList(products: List<ListItem>, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            updateProductListItemsUseCase.execute(mList.value!!.listToken, products) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadList(mList.value!!.listToken)
                    callback(it)
                }
            }
        }
    }

    fun updateProductListItem(itemIndex: Int, status: ItemStatus, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            updateProductListItemStatusUseCase.execute(mList.value!!.listToken, itemIndex, status) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadList(mList.value!!.listToken)
                    callback(it)
                }
            }
        }
    }

    fun lunchProductListSettings() {
        productListRouter.goToProductListSettings(mList.value!!.listToken)
    }

    fun lunchAboutFragment() {
        productListRouter.goToAboutProduct()
    }

    fun updateProductListName(newName: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            updateProductListNameUseCase.execute(mList.value!!.listToken, newName) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadList(mList.value!!.listToken)
                    callback(it)
                }
            }
        }
    }

}
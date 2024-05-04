package com.grishina.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.data.ProductList
import com.grishina.domain.share.usecase.DeleteProductListUseCase
import com.grishina.domain.share.usecase.LoadProductListsUseCase
import com.grishina.domain.share.usecase.UpdateProductListNameUseCase
import com.grishina.home.HomeRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRouter: HomeRouter,
    private val deleteProductListUseCase: DeleteProductListUseCase,
    private val loadProductListsUseCase: LoadProductListsUseCase,
    private val updateProductListNameUseCase: UpdateProductListNameUseCase
) : ViewModel() {
    private val mList = MutableLiveData<List<ProductList>>()
    val list: LiveData<List<ProductList>> = mList

    private var mUserToken: String? = null

    fun loadList(userToken: String) {
        mUserToken = userToken
        CoroutineScope(Dispatchers.IO).launch {
            loadProductListsUseCase.execute(userToken) { success, lists ->
                if (success) mList.postValue(lists)
                else mList.postValue(mutableListOf())
            }
        }
    }

    fun deleteProductList(listToken: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteProductListUseCase.execute(listToken, callback)
        }
    }

    fun updateListName(listToken: String, newName: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            updateProductListNameUseCase.execute(listToken, newName, callback)
        }
    }


    fun lunchFriends() {
        homeRouter.goToFriend()
    }

    fun lunchProfile() {
        homeRouter.goToProfile()
    }

    fun lunchProductList(listToken: String) {
        homeRouter.goToProductList(listToken)
    }


}
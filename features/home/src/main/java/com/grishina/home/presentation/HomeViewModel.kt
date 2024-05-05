package com.grishina.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.data.ProductList
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.CreateProductListUseCase
import com.grishina.domain.share.usecase.DeleteProductListUseCase
import com.grishina.domain.share.usecase.LoadProductListsUseCase
import com.grishina.domain.share.usecase.UpdateProductListNameUseCase
import com.grishina.home.HomeRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRouter: HomeRouter,
    private val createProductListUseCase: CreateProductListUseCase,
    private val deleteProductListUseCase: DeleteProductListUseCase,
    private val loadProductListsUseCase: LoadProductListsUseCase,
    private val updateProductListNameUseCase: UpdateProductListNameUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {
    private val mList = MutableLiveData<List<ProductList>>(listOf())
    val list: LiveData<List<ProductList>> = mList

    private var user: User? = null

    fun initUser(): Boolean {
        user = getUserUseCase.execute()
        return user != null
    }

    fun getUser(): User = user!!

    fun createList(title: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            createProductListUseCase.execute(ProductList(
                ownerToken = user!!.userToken,
                title = title
            )) {
                CoroutineScope(Dispatchers.Main).launch {
                    callback(it)
                }
            }
        }
    }

    fun loadList() {
        CoroutineScope(Dispatchers.IO).launch {
            user?.let {
                loadProductListsUseCase.execute(it.userToken) { success, lists ->
                    CoroutineScope(Dispatchers.Main).launch {
                        if (success) mList.postValue(lists)
                        else mList.postValue(mutableListOf())
                    }
                }
            }
        }
    }

    fun deleteProductList(listToken: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteProductListUseCase.execute(listToken) {
                CoroutineScope(Dispatchers.Main).launch { callback(it) }
            }
        }
    }

    fun updateListName(listToken: String, newName: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            updateProductListNameUseCase.execute(listToken, newName) {
                CoroutineScope(Dispatchers.Main).launch { callback(it) }
            }
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
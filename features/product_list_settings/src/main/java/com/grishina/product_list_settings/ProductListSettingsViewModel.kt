package com.grishina.product_list_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.data.ProductList
import com.grishina.domain.share.usecase.AddFriendToListUseCase
import com.grishina.domain.share.usecase.LoadFriendsNameUseCase
import com.grishina.domain.share.usecase.LoadProductListUseCase
import com.grishina.domain.share.usecase.RemoveFriendFromListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListSettingsViewModel(
    private val loadProductListUseCase: LoadProductListUseCase,
    private val loadFriendsNameUseCase: LoadFriendsNameUseCase,
    private val addFriendToListUseCase: AddFriendToListUseCase,
    private val removeFriendFromListUseCase: RemoveFriendFromListUseCase
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

    fun convertShareTokensToName(callback: (Boolean, List<Pair<String, String>>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            list.value?.let { loadFriendsNameUseCase.execute(it.sharedWith, callback) }
        }
    }

    fun addFriend(friendToken: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            list.value?.let { addFriendToListUseCase.execute(it.listToken, friendToken, callback) }
        }
    }

    fun removeFriend(friendToken: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            list.value?.let { removeFriendFromListUseCase.execute(it.listToken, friendToken, callback) }
        }
    }

}
package com.grishina.product_list_settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.FriendRequestStatus
import com.grishina.domain.data.ProductList
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.AddFriendToListUseCase
import com.grishina.domain.share.usecase.GetUserByTokenUseCase
import com.grishina.domain.share.usecase.LoadFriendRequestsUseCase
import com.grishina.domain.share.usecase.LoadFriendsNameUseCase
import com.grishina.domain.share.usecase.LoadProductListUseCase
import com.grishina.domain.share.usecase.RemoveFriendFromListUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListSettingsViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getUserByTokenUseCase: GetUserByTokenUseCase,
    private val loadProductListUseCase: LoadProductListUseCase,
    private val loadFriendsNameUseCase: LoadFriendsNameUseCase,
    private val addFriendToListUseCase: AddFriendToListUseCase,
    private val loadFriendRequestsUseCase: LoadFriendRequestsUseCase,
    private val removeFriendFromListUseCase: RemoveFriendFromListUseCase
) : ViewModel() {
    private val mList = MutableLiveData<ProductList>()
    val list: LiveData<ProductList> = mList

    private val mRequestList = MutableLiveData<List<FriendRequest>>(mutableListOf())
    private val requestList: LiveData<List<FriendRequest>> = mRequestList
    var listOfPairRequestsAndNames: List<Pair<FriendRequest, String>> = mutableListOf()

    private val user = getUserUseCase.execute()!!

    fun getUser() = user

    fun init(listToken: String) {
        loadList(listToken)
        loadRequests() {
            loadNames {  }
        }
    }

    fun loadList(listToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            loadProductListUseCase.execute(listToken) {
                CoroutineScope(Dispatchers.Main).launch {
                    mList.postValue(it)
                }
            }
        }
    }

    fun loadRequests(callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            loadFriendRequestsUseCase.execute(User(login = getUserUseCase.execute()!!.login).userToken) { _, listRequests ->
                val result = mutableListOf<FriendRequest>()
                listRequests!!.forEach { friendRequest ->
                    if (friendRequest.status == FriendRequestStatus.APPROVED)
                        result.add(friendRequest)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    mRequestList.postValue(result)
                    callback(true)
                }
            }
        }
    }

    fun addFriend(friendToken: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            addFriendToListUseCase.execute(mList.value!!.listToken, friendToken) {
                CoroutineScope(Dispatchers.IO).launch {
                    loadList(mList.value!!.listToken)
                    callback(it)
                }
            }
        }
    }

    fun removeFriend(friendToken: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            removeFriendFromListUseCase.execute(mList.value!!.listToken, friendToken) {
                CoroutineScope(Dispatchers.IO).launch {
                    loadList(mList.value!!.listToken)
                    callback(it)
                }
            }
        }
    }

    fun getUserByToken(token: String, callback: (User?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            getUserByTokenUseCase.execute(token) {
                CoroutineScope(Dispatchers.Main).launch {
                    callback(it)
                }
            }
        }
    }

    fun loadNames(callback: ((List<Pair<FriendRequest, String>>)->Unit)?) {
        val result = mutableListOf<Pair<FriendRequest, String>>()
        if (mRequestList.value == null) return
        val items = mRequestList.value!!
        val tokens = mutableListOf<String>()

        items.forEach {
            if (it.fromToken != user.userToken)
                tokens.add(it.fromToken)
            else tokens.add(it.toToken)
        }
        CoroutineScope(Dispatchers.IO).launch {
            loadFriendsNameUseCase.execute(tokens) { _, tokenToNameList ->
                CoroutineScope(Dispatchers.IO).launch {
                    items.forEach { friendRequest ->
                        result.add(Pair(
                            friendRequest,
                            tokenToNameList!![tokenToNameList.indexOfFirst {
                                it.first == friendRequest.fromToken && it.first != user.userToken ||
                                        it.first == friendRequest.toToken && it.first != user.userToken
                            }].second
                        ))
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        listOfPairRequestsAndNames = result
                        if (callback != null) {
                            callback(result)
                        }
                    }
                }
            }
        }
    }

}
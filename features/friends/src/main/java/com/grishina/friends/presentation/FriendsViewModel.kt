package com.grishina.friends.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.FriendRequestStatus
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.AcceptFriendRequestUseCase
import com.grishina.domain.share.usecase.LoadFriendRequestsUseCase
import com.grishina.domain.share.usecase.LoadFriendsNameUseCase
import com.grishina.domain.share.usecase.RefuseFriendRequestUseCase
import com.grishina.domain.share.usecase.RemoveFriendUseCase
import com.grishina.domain.share.usecase.SendFriendRequestUseCase
import com.grishina.friends.FriendsRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val friendsRouter: FriendsRouter,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val loadFriendRequestsUseCase: LoadFriendRequestsUseCase,
    private val loadFriendsNameUseCase: LoadFriendsNameUseCase,
    private val refuseFriendRequestUseCase: RefuseFriendRequestUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val mList = MutableLiveData<List<FriendRequest>>(listOf())
    val list: LiveData<List<FriendRequest>> = mList

    var listOfPairRequestsAndNames: List<Pair<FriendRequest, String>>? = null
    private val user = getUserUseCase.execute()!!

    fun getUser(): User = user

    fun loadRequests() {
        CoroutineScope(Dispatchers.IO).launch {
            loadFriendRequestsUseCase.execute(User(login = getUserUseCase.execute()!!.login).userToken) { _, listRequests ->
                val result = mutableListOf<FriendRequest>()
                listRequests!!.forEach { friendRequest ->
                    if ((
                        friendRequest.fromToken != user.userToken ||
                        friendRequest.status == FriendRequestStatus.APPROVED) &&
                        friendRequest.status != FriendRequestStatus.REFUSED
                        )
                        result.add(friendRequest)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    mList.postValue(result)
                }
            }
        }
    }

    fun acceptRequest(request: FriendRequest, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            acceptFriendRequestUseCase.execute(request) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadRequests()
                    callback(it)
                }
            }
        }
    }

    fun refuseRequest(request: FriendRequest, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            refuseFriendRequestUseCase.execute(request) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadRequests()
                    callback(it)
                }
            }
        }
    }

    fun removeFriend(request: FriendRequest, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            removeFriendUseCase.execute(request) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadRequests()
                    callback(it)
                }
            }
        }
    }

    fun sendRequest(toToken: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            sendFriendRequestUseCase.execute(getUserUseCase.execute()!!.userToken, toToken) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadRequests()
                    callback(it)
                }
            }
        }
    }

    fun loadNames(callback: (List<Pair<FriendRequest, String>>)->Unit) {
        val result = mutableListOf<Pair<FriendRequest, String>>()
        val items = list.value!!
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
                        callback(result)
                    }
                }
            }
        }
    }

    fun lunchHome() {
        friendsRouter.goToHome()
    }

    fun lunchProfile() {
        friendsRouter.goToProfile()
    }

}
package com.grishina.friends.presentation

import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.data.FriendRequest
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

    fun loadRequests(callback: (Boolean, List<FriendRequest>?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            loadFriendRequestsUseCase.execute(User(login = getUserUseCase.execute()!!.login).userToken, callback)
        }
    }

    fun acceptRequest(request: FriendRequest, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            acceptFriendRequestUseCase.execute(request, callback)
        }
    }

    fun refuseRequest(request: FriendRequest, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            refuseFriendRequestUseCase.execute(request, callback)
        }
    }

    fun removeFriend(request: FriendRequest, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            removeFriendUseCase.execute(request, callback)
        }
    }

    fun sendRequest(toToken: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            sendFriendRequestUseCase.execute(getUserUseCase.execute()!!.userToken, toToken, callback)
        }
    }

    fun loadNames(callback: (Boolean, List<Pair<String, String>>?)->Unit) {
        loadRequests() { success, list ->
            if (!success) callback(false, null)
            val tokens = mutableListOf<String>()
            list!!.forEach {
                if (it.fromToken != getUserUseCase.execute()!!.userToken)
                    tokens.add(it.fromToken)
                else tokens.add(it.toToken)
            }
            CoroutineScope(Dispatchers.IO).launch {
                loadFriendsNameUseCase.execute(tokens, callback)
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
package com.grishina.data.share

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.FriendRequestStatus
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.domain.data.ProductList
import com.grishina.domain.data.User
import com.grishina.domain.share.ShareRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.Random

class ShareRepositoryImpl: ShareRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun createProductList(productList: ProductList, successCallback: (Boolean)->Unit) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == productList.listToken) {
                            successCallback(false)
                            return
                        }
                }
                query.child(productList.listToken).setValue(productList){ error, _ ->
                    if (error != null) {
                        Log.e(TAG, "Failed to update list", error.toException())
                        successCallback(false)
                    } else {
                        successCallback(true)
                    }
                }
                return
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun updateProductListName(
        listToken: String,
        newName: String,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            list.title = newName
                            productLists.ref.setValue(list) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update list", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun deleteProductList(listToken: String, successCallback: (Boolean)->Unit) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            productLists.ref.removeValue() { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update list", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun addFriendToList(
        listToken: String,
        friendToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            if (list.sharedWith.contains(friendToken)) {
                                Log.w(TAG, "List contains that friend token")
                                successCallback(false)
                                return
                            }

                            val temp = list.sharedWith.toMutableList()
                            temp.add(friendToken)
                            list.sharedWith = temp

                            productLists.ref.setValue(list) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update list", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }


    override suspend fun removeFriendFromList(
        listToken: String,
        friendToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            val temp = list.sharedWith.toMutableList()
                            temp.remove(friendToken)
                            list.sharedWith = temp

                            productLists.ref.setValue(list) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update list", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun updateProductListItems(
        listToken: String,
        items: List<ListItem>,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            list.items = items

                            productLists.ref.setValue(list) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update list", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun updateProductListItemStatus(
        listToken: String,
        itemIndex: Int,
        status: ItemStatus,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            list.items[itemIndex].status = status

                            productLists.ref.setValue(list) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update list", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun sendFriendRequest(
        fromToken: String,
        toToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(REQUEST_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var continueCreate = true
                for (requests in dataSnapshot.children) {
                    val request = requests.getValue(FriendRequest::class.java)
                    if (request != null)
                        if (request.fromToken == fromToken && request.toToken == toToken ||
                            request.fromToken == toToken && request.toToken == fromToken) {
                            if (request.status != FriendRequestStatus.WAITING) {
                                request.status = FriendRequestStatus.WAITING
                                requests.ref.setValue(request) { error, _ ->
                                    if (error != null) {
                                        Log.e(TAG, "Failed to send request", error.toException())
                                        successCallback(false)
                                    } else {
                                        successCallback(true)
                                    }
                                }
                            } else if (request.toToken == fromToken) {
                                request.status = FriendRequestStatus.APPROVED
                                requests.ref.setValue(request) { error, _ ->
                                    if (error != null) {
                                        Log.e(TAG, "Failed to send request", error.toException())
                                        successCallback(false)
                                    } else {
                                        successCallback(true)
                                    }
                                }
                            }
                            continueCreate = false
                        }
                }
                if (!continueCreate) return
                val newRequest = FriendRequest(fromToken, toToken)
                query.child(newRequest.requestToken).setValue(newRequest) { error, _ ->
                    if (error != null) {
                        Log.e(TAG, "Failed to send request [new request]", error.toException())
                        successCallback(false)
                    } else {
                        successCallback(true)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun acceptFriendRequest(
        request: FriendRequest,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(REQUEST_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (requests in dataSnapshot.children) {
                    val friendRequest = requests.getValue(FriendRequest::class.java)
                    if (friendRequest != null)
                        if (friendRequest.fromToken == request.fromToken &&
                            friendRequest.toToken == request.toToken) {

                            friendRequest.status = FriendRequestStatus.APPROVED
                            requests.ref.setValue(friendRequest) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to approve request", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                Log.e(TAG, "Failed to approve request")
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun refuseFriendRequest(
        request: FriendRequest,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(REQUEST_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (requests in dataSnapshot.children) {
                    val friendRequest = requests.getValue(FriendRequest::class.java)
                    if (friendRequest != null)
                        if (friendRequest.fromToken == request.fromToken &&
                            friendRequest.toToken == request.toToken) {

                            friendRequest.status = FriendRequestStatus.REFUSED
                            requests.ref.setValue(friendRequest) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to refuse request", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                Log.e(TAG, "Failed to refuse request")
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun removeFriend(
        request: FriendRequest,
        successCallback: (Boolean) -> Unit,
    ) {
        val query = database.reference.child(REQUEST_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (requests in dataSnapshot.children) {
                    val friendRequest = requests.getValue(FriendRequest::class.java)
                    if (friendRequest != null)
                        if (friendRequest.fromToken == request.fromToken &&
                            friendRequest.toToken == request.toToken ||
                            friendRequest.toToken == request.fromToken &&
                            friendRequest.fromToken == request.toToken) {

                            friendRequest.status = FriendRequestStatus.REFUSED
                            requests.ref.setValue(friendRequest) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to refuse request", error.toException())
                                    successCallback(false)
                                } else {
                                    successCallback(true)
                                }
                            }
                            return
                        }
                }
                Log.e(TAG, "Failed to refuse request")
                successCallback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                successCallback(false)
            }
        })
    }

    override suspend fun loadFriendsName(friendsTokens: List<String>, callback: (Boolean, List<Pair<String, String>>?)->Unit) {
        val names = mutableListOf<Pair<String, String>>()
        val query = database.reference.child(USER_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (users in snapshot.children) {
                    val user = users.getValue(User::class.java)
                    if (user != null)
                        if (friendsTokens.contains(user.userToken))  {
                            names.add(user.userToken to user.name)
                        }
                }
                callback(true, names)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(false, null)
            }
        })
    }

    override suspend fun loadFriendRequests(myToken: String, callback: (Boolean, List<FriendRequest>?)->Unit) {
        val fRequests = mutableListOf<FriendRequest>()
        val query = database.reference.child(REQUEST_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (requests in dataSnapshot.children) {
                    val friendRequest = requests.getValue(FriendRequest::class.java)
                    if (friendRequest != null)
                        if (friendRequest.fromToken == myToken ||
                            friendRequest.toToken == myToken) {
                            fRequests.add(friendRequest)
                        }
                }
                callback(true, fRequests)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(false, null)
            }
        })
    }

    override suspend fun loadProductLists(myToken: String, callback: (Boolean, List<ProductList>?)->Unit) {
        val productLists = mutableListOf<ProductList>()
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productList in dataSnapshot.children) {
                    val list = productList.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.ownerToken == myToken ||
                            list.sharedWith.contains(myToken)) {
                            productLists.add(list)
                        }
                }
                callback(true, productLists)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(false, null)
            }
        })
    }

    override suspend fun loadProductList(listToken: String, callback: (ProductList?) -> Unit) {
        val query = database.reference.child(PRODUCT_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productList in dataSnapshot.children) {
                    val list = productList.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.listToken == listToken) {
                            callback(list)
                            return
                        }
                }
                callback(null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(null)
            }
        })
    }

    override suspend fun observeListChanges(listToken: String, callback: (ProductList?) -> Unit) {
        val query = database.reference.child(PRODUCT_REF)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productList in dataSnapshot.children) {
                    val list = productList.getValue(ProductList::class.java)
                    if (list != null && list.listToken == listToken) {
                        callback(list)
                        return
                    }
                }
                callback(null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(null)
            }
        })
    }

    override suspend fun authInRTDB(user: User, callback: (Boolean, User?) -> Unit) {
        val query = database.reference.child(USER_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (users in dataSnapshot.children) {
                    val userFB = users.getValue(User::class.java)
                    if (userFB != null) {
                        if (userFB.login == user.login) {
                            callback(true, userFB)
                            return
                        }
                    }
                }
                callback(false, null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(false, null)
            }
        })
    }

    override suspend fun registerInRTDB(user: User, callback: (Boolean) -> Unit) {
        val query = database.reference.child(USER_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (users in dataSnapshot.children) {
                    val userFB = users.getValue(User::class.java)
                    if (userFB != null) {
                        if (userFB.login == user.login) {
                            callback(false)
                            return
                        }
                    }
                }
                dataSnapshot.ref.child(user.userToken).setValue(User(user.login, "", user.name))
                callback(true)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(false)
            }
        })
    }

    override suspend fun updateName(userToken: String, name: String, callback: (Boolean) -> Unit) {
        val query = database.reference.child(USER_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (users in dataSnapshot.children) {
                    val userFB = users.getValue(User::class.java)
                    if (userFB != null) {
                        if (userFB.userToken == userToken) {
                            userFB.name = name
                            users.ref.setValue(userFB) { error, _ ->
                                if (error != null) {
                                    Log.e(TAG, "Failed to update name request", error.toException())
                                    callback(false)
                                } else {
                                    callback(true)
                                }
                            }
                            return
                        }
                    }
                }
                callback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(false)
            }
        })
    }

    override suspend fun getUserByToken(userToken: String, callback: (User?) -> Unit) {
        val query = database.reference.child(USER_REF)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (users in dataSnapshot.children) {
                    val userFB = users.getValue(User::class.java)
                    if (userFB != null) {
                        if (userFB.userToken == userToken) {
                            callback(userFB)
                            return
                        }
                    }
                }
                callback(null)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException())
                callback(null)
            }
        })
    }


    companion object {
        private const val TAG = "ShareRepositoryImpl"
        private const val USER_REF = "users"
        private const val PRODUCT_REF = "products"
        private const val REQUEST_REF = "requests"
    }
}
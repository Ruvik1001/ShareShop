package com.grishina.data.share

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.domain.data.ProductList
import com.grishina.domain.share.ShareRepository
import kotlinx.coroutines.tasks.await

class ShareRepositoryImpl: ShareRepository {
    private val database = FirebaseDatabase.getInstance()

    override suspend fun createProductList(productList: ProductList): Boolean {
        val ref = database.reference.child(PRODUCT_REF).push()
        return try {
            ref.setValue(productList).await()
            true
        } catch (e : Exception) {
            Log.e(TAG, e.stackTrace.toString())
            false
        }
    }

    override suspend fun deleteProductList(ownerToken: String, title: String, successCallback: (Boolean)->Unit) {
        val query = database.reference.child(PRODUCT_REF).orderByChild("ownerToken").equalTo(ownerToken)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (productLists in dataSnapshot.children) {
                    val list = productLists.getValue(ProductList::class.java)
                    if (list != null)
                        if (list.ownerToken == ownerToken && list.title == title) {
                            productLists.ref.removeValue()
                            successCallback(true)
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
        ownerToken: String,
        title: String,
        friendToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFriendFromList(
        ownerToken: String,
        title: String,
        friendToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProductListItems(
        ownerToken: String,
        title: String,
        items: List<ListItem>,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateProductListItemStatus(
        ownerToken: String,
        title: String,
        itemName: String,
        status: ItemStatus,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun sendFriendRequest(
        fromToken: String,
        toToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun acceptFriendRequest(
        request: FriendRequest,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun refuseFriendRequest(
        request: FriendRequest,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFriend(
        fromToken: String,
        toToken: String,
        successCallback: (Boolean) -> Unit,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun loadFriendsName(friendsTokens: List<String>): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun loadFriendRequests(myToken: String): List<FriendRequest> {
        TODO("Not yet implemented")
    }

    override suspend fun loadProductLists(myToken: String): List<ProductList> {
        TODO("Not yet implemented")
    }


    companion object {
        private const val TAG = "ShareRepositoryImpl"
        private const val USER_REF = "users"
        private const val PRODUCT_REF = "products"
        private const val REQUEST_REF = "requests"
    }
}
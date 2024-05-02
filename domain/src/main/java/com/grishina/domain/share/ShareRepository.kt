package com.grishina.domain.share

import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.domain.data.ProductList

interface ShareRepository {
    // Owner action
    suspend fun createProductList(productList: ProductList): Boolean
    suspend fun deleteProductList(ownerToken: String, title: String, successCallback: (Boolean)->Unit)

    suspend fun addFriendToList(ownerToken: String, title: String, friendToken: String, successCallback: (Boolean)->Unit)
    suspend fun removeFriendFromList(ownerToken: String, title: String, friendToken: String, successCallback: (Boolean)->Unit)

    // Owner and friends action
    suspend fun updateProductListItems(ownerToken: String, title: String, items: List<ListItem>, successCallback: (Boolean)->Unit)
    suspend fun updateProductListItemStatus(ownerToken: String, title: String, itemName: String, status: ItemStatus, successCallback: (Boolean)->Unit)

    // All users
    suspend fun sendFriendRequest(fromToken: String, toToken: String, successCallback: (Boolean)->Unit)
    suspend fun acceptFriendRequest(request: FriendRequest, successCallback: (Boolean)->Unit)
    suspend fun refuseFriendRequest(request: FriendRequest, successCallback: (Boolean)->Unit)
    suspend fun removeFriend(fromToken: String, toToken: String, successCallback: (Boolean)->Unit)

    // Current user
    suspend fun loadFriendsName(friendsTokens: List<String>): List<String> // Names
    suspend fun loadFriendRequests(myToken: String): List<FriendRequest>
    suspend fun loadProductLists(myToken: String): List<ProductList>
}

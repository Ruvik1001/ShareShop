package com.grishina.domain.share

import com.grishina.domain.data.FriendRequest
import com.grishina.domain.data.ItemStatus
import com.grishina.domain.data.ListItem
import com.grishina.domain.data.ProductList
import com.grishina.domain.data.User

interface ShareRepository {
    // Owner action
    suspend fun createProductList(productList: ProductList, successCallback: (Boolean)->Unit)
    suspend fun updateProductListName(listToken: String, newName: String, successCallback: (Boolean) -> Unit)
    suspend fun deleteProductList(listToken: String, successCallback: (Boolean)->Unit)

    suspend fun addFriendToList(listToken: String, friendToken: String, successCallback: (Boolean)->Unit)
    suspend fun removeFriendFromList(listToken: String, friendToken: String, successCallback: (Boolean)->Unit)

    // Owner and friends action
    suspend fun updateProductListItems(listToken: String, items: List<ListItem>, successCallback: (Boolean)->Unit)
    suspend fun updateProductListItemStatus(listToken: String, itemIndex: Int, status: ItemStatus, successCallback: (Boolean)->Unit)

    // All users
    suspend fun sendFriendRequest(fromToken: String, toToken: String, successCallback: (Boolean)->Unit)
    suspend fun acceptFriendRequest(request: FriendRequest, successCallback: (Boolean)->Unit)
    suspend fun refuseFriendRequest(request: FriendRequest, successCallback: (Boolean)->Unit)
    suspend fun removeFriend(request: FriendRequest, successCallback: (Boolean)->Unit)

    // Current user
    suspend fun loadFriendsName(friendsTokens: List<String>, callback: (Boolean, List<Pair<String, String>>?)->Unit) // Names
    suspend fun loadFriendRequests(myToken: String, callback: (Boolean, List<FriendRequest>?)->Unit)
    suspend fun loadProductLists(myToken: String, callback: (Boolean, List<ProductList>?)->Unit)
    suspend fun loadProductList(listToken: String, callback: (ProductList?) -> Unit)

    suspend fun authInRTDB(user: User, callback: (Boolean, User?)->Unit)
    suspend fun registerInRTDB(user: User, callback: (Boolean)->Unit)
    suspend fun updateName(userToken: String, name: String, callback: (Boolean) -> Unit)

}

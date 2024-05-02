package com.grishina.domain.data

data class ProductList(
    var ownerToken: String = "",
    var title: String = "",
    var items: List<ListItem> = listOf(),
    var sharedWith: List<String> = listOf()
)
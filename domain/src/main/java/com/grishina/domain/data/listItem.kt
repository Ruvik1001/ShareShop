package com.grishina.domain.data

enum class ItemStatus {
    ACTIVE,
    BOUGHT
}

data class ListItem(var name: String = "", var status: ItemStatus = ItemStatus.ACTIVE)

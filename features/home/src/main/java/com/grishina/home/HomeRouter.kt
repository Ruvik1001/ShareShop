package com.grishina.home

interface HomeRouter {
    fun goToFriend()

    fun goToProfile()

    fun goToProductList(listToken: String)
}
package com.grishina.shareshop.glue

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.grishina.home.HomeRouter
import com.grishina.shareshop.R

class AdapterHomeRouter(
    private val context: Context,
    private var navController: NavController?
) : HomeRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToFriend() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        navController?.navigate(R.id.action_homeFragment_to_friendsFragment, null, navOptions)
    }

    override fun goToProfile() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        navController?.navigate(R.id.action_homeFragment_to_profileFragment, null, navOptions)
    }

    override fun goToProductList(listToken: String) {
        val bundle = Bundle().apply {
            putString(
                context.getString(com.grishina.core.R.string.LIST_TOKEN_KEY_FROM_HOME),
                listToken
            )
        }
        navController?.navigate(R.id.action_homeFragment_to_productListFragment, bundle)
    }

}
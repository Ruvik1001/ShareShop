package com.grishina.shareshop.glue

import android.content.Context
import android.os.Bundle
import androidx.navigation.NavController
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
        navController?.navigate(R.id.action_homeFragment_to_friendsFragment)
        navController?.popBackStack(R.id.friendsFragment, false)
    }

    override fun goToProfile() {
        navController?.navigate(R.id.action_homeFragment_to_profileFragment)
        navController?.popBackStack(R.id.profileFragment, false)
    }

    override fun goToProductList(listToken: String) {
        val bundle = Bundle().apply {
            putString(
                context.getString(com.grishina.core.R.string.LIST_TOKEN_KEY_FROM_HOME),
                listToken
            )
        }
        navController?.navigate(R.id.action_homeFragment_to_productListFragment, bundle)
        navController?.popBackStack(R.id.productListFragment, false)
    }

}
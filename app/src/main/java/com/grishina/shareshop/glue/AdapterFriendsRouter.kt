package com.grishina.shareshop.glue

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.grishina.friends.FriendsRouter
import com.grishina.shareshop.R

class AdapterFriendsRouter(
    private var navController: NavController?
) : FriendsRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.friendsFragment, true)
            .build()
        navController?.navigate(R.id.action_friendsFragment_to_homeFragment, null, navOptions)
    }

    override fun goToProfile() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.friendsFragment, true)
            .build()
        navController?.navigate(R.id.action_friendsFragment_to_profileFragment, null, navOptions)
    }
}
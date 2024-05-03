package com.grishina.shareshop.glue

import androidx.navigation.NavController
import com.grishina.friends.FriendsRouter
import com.grishina.shareshop.R

class AdapterFriendsRouter(
    private var navController: NavController?
) : FriendsRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToHome() {
        navController?.navigate(R.id.action_friendsFragment_to_homeFragment)
        navController?.popBackStack(R.id.homeFragment, false)
    }

    override fun goToProfile() {
        navController?.navigate(R.id.action_friendsFragment_to_profileFragment)
        navController?.popBackStack(R.id.profileFragment, false)
    }
}
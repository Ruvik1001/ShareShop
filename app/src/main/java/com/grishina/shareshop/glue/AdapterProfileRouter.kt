package com.grishina.shareshop.glue

import androidx.navigation.NavController
import com.grishina.profile.ProfileRouter
import com.grishina.shareshop.R

class AdapterProfileRouter(
    private var navController: NavController?
) : ProfileRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToHome() {
        navController?.navigate(R.id.action_profileFragment_to_homeFragment)
        navController?.popBackStack(R.id.homeFragment, false)
    }

    override fun goToFriends() {
        navController?.navigate(R.id.action_profileFragment_to_friendsFragment)
        navController?.popBackStack(R.id.friendsFragment, false)
    }

    override fun goToSignIn() {
        navController?.navigate(R.id.action_profileFragment_to_signInFragment)
        navController?.popBackStack(R.id.signInFragment, false)
    }
}
package com.grishina.shareshop.glue

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.grishina.profile.ProfileRouter
import com.grishina.shareshop.R

class AdapterProfileRouter(
    private var navController: NavController?
) : ProfileRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.profileFragment, true)
            .build()
        navController?.navigate(R.id.action_profileFragment_to_homeFragment, null, navOptions)
    }

    override fun goToFriends() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.profileFragment, true)
            .build()
        navController?.navigate(R.id.action_profileFragment_to_friendsFragment, null, navOptions)
    }

    override fun goToSignIn() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.profileFragment, true)
            .build()
        navController?.navigate(R.id.action_profileFragment_to_signInFragment, null, navOptions)
    }
}
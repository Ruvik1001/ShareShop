package com.grishina.shareshop.glue

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.grishina.shareshop.R
import com.grishina.sign_in.SignInRouter

class AdapterSignInRouter(
    private var navController: NavController?
) : SignInRouter {

    fun switchNavController(navControllerNew: NavController) {
        navController = navControllerNew
    }

    override fun goToSignUp() {
        navController?.navigate(R.id.action_signInFragment_to_signUpFragment)
    }

    override fun goToPasswordReset() {
        navController?.navigate(R.id.action_signInFragment_to_resetPasswordFragment)
    }

    override fun goToHome() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.signInFragment, true)
            .build()
        navController?.navigate(R.id.action_signInFragment_to_homeFragment, null, navOptions)
    }
}
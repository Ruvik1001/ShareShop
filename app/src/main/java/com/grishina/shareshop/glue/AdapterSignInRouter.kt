package com.grishina.shareshop.glue

import androidx.navigation.NavController
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
        navController?.popBackStack(R.id.signUpFragment, false)
    }

    override fun goToPasswordReset() {
        navController?.navigate(R.id.action_signInFragment_to_resetPasswordFragment)
        navController?.popBackStack(R.id.resetPasswordFragment, false)
    }

    override fun goToHome() {
        navController?.navigate(R.id.action_signInFragment_to_homeFragment)
        navController?.popBackStack(R.id.homeFragment, false)
    }
}
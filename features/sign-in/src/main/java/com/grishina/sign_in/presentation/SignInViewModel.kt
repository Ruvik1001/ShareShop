package com.grishina.sign_in.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.auth.usecase.SignInUseCase
import com.grishina.domain.data.User
import com.grishina.sign_in.R
import com.grishina.sign_in.SignInRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val signInRouter: SignInRouter,
    private val signInUseCase: SignInUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    fun continueIfUserSignInBefore() {
        if (getUserUseCase.execute() != null)
            lunchHome()
    }

    fun signIn(
        login: String,
        password: String,
        callback: (Boolean)->Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = signInUseCase.execute(User(login = login, password = password))
            withContext(Dispatchers.Main) {
                callback(result != null)
            }
        }
    }

    fun lunchHome() {
        signInRouter.goToHome()
    }

    fun lunchSignUp() {
        signInRouter.goToSignUp()
    }

    fun lunchForgotPassword() {
        signInRouter.goToPasswordReset()
    }
}
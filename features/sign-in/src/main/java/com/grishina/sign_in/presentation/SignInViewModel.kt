package com.grishina.sign_in.presentation

import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.SignInUseCase
import com.grishina.domain.data.User
import com.grishina.sign_in.SignInRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val signInRouter: SignInRouter
) : ViewModel() {
    fun signIn(
        login: String,
        password: String,
        callback: (Boolean)->Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            callback(signInUseCase.execute(User(login = login, password = password)) != null)
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
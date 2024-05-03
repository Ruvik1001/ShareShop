package com.grishina.sign_up

import android.content.Context
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.SignUpUseCase
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.RegisterInRTDBUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val context: Context,
    private val signUpUseCase: SignUpUseCase,
    private val registerInRTDBUseCase: RegisterInRTDBUseCase
) : ViewModel() {

    fun signUp(
        user: User,
        callback: (Boolean, Boolean)->Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (!signUpUseCase.execute(User(login = user.login, password = user.password))) {
                callback(false, false)
                return@launch
            }
            registerInRTDBUseCase.execute(User(login = user.login, name = user.name)) {
                callback(true, it)
            }
        }
    }

    fun validateMailPattern(login: String): Boolean {
        val emailPattern = context.getString(R.string.mailPattern).toRegex()
        return emailPattern.matches(login)
    }

    fun validatePasswordPattern(password: String): Boolean {
        return password.length >= 6
    }
}
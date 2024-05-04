package com.grishina.sign_up

import android.content.Context
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.SignUpUseCase
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.RegisterInRTDBUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val registerInRTDBUseCase: RegisterInRTDBUseCase
) : ViewModel() {

    fun signUpInAuth(
        user: User,
        callback: (Boolean)->Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = signUpUseCase.execute(User(login = user.login, password = user.password))
            withContext(Dispatchers.Main) { callback(result) }
        }
    }

    fun signUpInRTDB(
        user: User,
        callback: (Boolean)->Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            registerInRTDBUseCase.execute(User(login = user.login, name = user.name)) {
                CoroutineScope(Dispatchers.Main).launch {
                    callback(it)
                }
            }
        }
    }
}
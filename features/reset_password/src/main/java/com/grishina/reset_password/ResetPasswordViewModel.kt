package com.grishina.reset_password

import android.content.Context
import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.ResetPasswordUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    fun resetPassword(login: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = resetPasswordUseCase.execute(login)
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }
}
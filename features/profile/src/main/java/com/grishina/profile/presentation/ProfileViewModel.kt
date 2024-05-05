package com.grishina.profile.presentation

import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.GetUserUseCase
import com.grishina.domain.auth.usecase.SetNewPasswordUseCase
import com.grishina.domain.auth.usecase.SignOutUseCase
import com.grishina.domain.data.User
import com.grishina.domain.share.usecase.AuthInRTDBUseCase
import com.grishina.domain.share.usecase.UpdateNameUseCase
import com.grishina.profile.ProfileRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val profileRouter: ProfileRouter,
    private val signOutUseCase: SignOutUseCase,
    private val setNewPasswordUseCase: SetNewPasswordUseCase,
    private val updateNameUseCase: UpdateNameUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val authInRTDBUseCase: AuthInRTDBUseCase
) : ViewModel() {
    fun getUser(callback: (User?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            authInRTDBUseCase.execute(getUserUseCase.execute()!!.login) { _, user ->
                CoroutineScope(Dispatchers.Main).launch {
                    callback(user)
                }
            }
        }
    }

    fun setNewPassword(password: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = setNewPasswordUseCase.execute(password)
            withContext(Dispatchers.Main) {
                callback(result)
            }
        }
    }

    fun updateName(newName: String, callback: (Boolean) -> Unit) {
        getUser {
            CoroutineScope(Dispatchers.IO).launch {
                updateNameUseCase.execute(it!!.userToken, newName) {
                    CoroutineScope(Dispatchers.Main).launch {
                        callback(it)
                    }
                }
            }
        }
    }

    fun lunchHome() {
        profileRouter.goToHome()
    }

    fun lunchFriends() {
        profileRouter.goToFriends()
    }

    fun logOut() {
        signOutUseCase.execute()
        profileRouter.goToSignIn()
    }
}
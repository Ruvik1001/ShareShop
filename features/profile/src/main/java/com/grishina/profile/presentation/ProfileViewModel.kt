package com.grishina.profile.presentation

import androidx.lifecycle.ViewModel
import com.grishina.domain.auth.usecase.SetNewPasswordUseCase
import com.grishina.domain.auth.usecase.SignOutUseCase
import com.grishina.domain.share.usecase.UpdateNameUseCase
import com.grishina.profile.ProfileRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRouter: ProfileRouter,
    private val signOutUseCase: SignOutUseCase,
    private val setNewPasswordUseCase: SetNewPasswordUseCase,
    private val updateNameUseCase: UpdateNameUseCase
) : ViewModel() {

    fun setNewPassword(password: String, callback: (Boolean)->Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            callback(setNewPasswordUseCase.execute(password))
        }
    }

    fun updateName(userToken: String, newName: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            updateNameUseCase.execute(userToken, newName, callback)
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
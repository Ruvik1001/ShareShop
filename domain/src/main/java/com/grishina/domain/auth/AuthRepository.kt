package com.grishina.domain.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.SignInMethodQueryResult
import com.grishina.domain.data.User

interface AuthRepository {

    suspend fun fetchSignInMethodsForEmail(email: String): SignInMethodQueryResult

    suspend fun signIn(user: User): Task<AuthResult>

    suspend fun signUp(user: User): Task<AuthResult>

    suspend fun resetPassword(email: String): Task<Void>

    suspend fun setNewPassword(password: String): Task<AuthResult>

    fun signOut()

    fun getUser(): FirebaseUser?
}
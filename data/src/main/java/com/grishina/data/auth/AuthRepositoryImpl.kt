package com.grishina.data.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.grishina.domain.auth.AuthRepository
import com.grishina.domain.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl: AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun fetchSignInMethodsForEmail(email: String): SignInMethodQueryResult {
        return firebaseAuth.fetchSignInMethodsForEmail(email).await()
    }

    override suspend fun signIn(user: User): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(user.login, user.password)
    }

    override suspend fun signUp(user: User): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(user.login, user.password)
    }

    override suspend fun resetPassword(email: String): Task<Void> {
        return firebaseAuth.sendPasswordResetEmail(email)
    }

    override suspend fun setNewPassword(password: String): Task<AuthResult> {
        val login = firebaseAuth.currentUser!!.email!!
        val auth = FirebaseAuth.getInstance()
        val deleteTask = firebaseAuth.currentUser?.delete()
        deleteTask?.await()
        return auth.createUserWithEmailAndPassword(login, password)
    }

}
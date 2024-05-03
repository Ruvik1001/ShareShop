package com.grishina.domain.data

import java.security.MessageDigest

data class User(
    var login: String = "",
    var password: String = "",
    var name: String = "",
    var userToken: String = "",
) {
    init {
        login = login.replace(".", "_")
        userToken = generateToken(login)
    }

    private fun generateToken(login: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(login.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }.substring(0, 16)
    }

}

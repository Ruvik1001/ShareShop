package com.grishina.domain.data

import java.security.MessageDigest

enum class FriendRequestStatus {
    APPROVED,
    REFUSED,
    WAITING
}

data class FriendRequest(
    var fromToken: String = "",
    var toToken: String = "",
    var status: FriendRequestStatus = FriendRequestStatus.WAITING,
    var requestToken: String = ""
) {
    init {
        requestToken = generateToken(fromToken)
    }

    private fun generateToken(login: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(login.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }.substring(0, 16)
    }

}

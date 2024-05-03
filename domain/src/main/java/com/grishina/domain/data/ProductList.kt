package com.grishina.domain.data

import java.security.MessageDigest
import java.util.*

data class ProductList(
    var ownerToken: String = "",
    var title: String = "",
    var items: List<ListItem> = listOf(),
    var sharedWith: List<String> = listOf(),
    var creationDate: Long = System.currentTimeMillis(),
    var listToken: String = ""
) {
    init {
        listToken = generateToken(ownerToken, creationDate.toString())
    }

    private fun generateToken(seed: String, salt: String): String {
        val saltedSeed = "$seed$salt"
        val bytes = MessageDigest.getInstance("SHA-256").digest(saltedSeed.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}

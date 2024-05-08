package com.kborowy.authprovider.firebase

/**
 * Replicate of FirebaseToken from Firebase admin module
 * to not expose transitive dependencies
 */
data class FirebaseToken(
    val uid: String,
    val issuer: String,
    val name: String,
    val picture: String,
    val email: String,
    val claims: Map<String, Any>?,
    val isEmailVerified: Boolean
)

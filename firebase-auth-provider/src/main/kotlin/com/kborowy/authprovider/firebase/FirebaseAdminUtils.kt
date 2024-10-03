package com.kborowy.authprovider.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream

internal class FirebaseAdminUtils(adminFileStream: InputStream) {

    constructor(adminFile: File) : this(adminFile.inputStream())

    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    private val firebaseOptions: FirebaseOptions by lazy {
        FirebaseOptions.builder().run {
            val credentials = GoogleCredentials.fromStream(adminFileStream)
            setCredentials(credentials)
            build()
        }
    }

    private val app: FirebaseApp by lazy {
        FirebaseApp.initializeApp(firebaseOptions)
    }

    suspend fun authenticateToken(token: String): FirebaseToken? {
        return try {
            FirebaseAuth.getInstance(app).verifyIdTokenAsync(token)
                .await()
                ?.let { adminToken ->
                    adminToken.claims
                    FirebaseToken(
                        uid = adminToken.uid,
                        issuer = adminToken.issuer,
                        name = adminToken.name,
                        picture = adminToken.picture,
                        email = adminToken.email,
                        claims = adminToken.claims,
                        isEmailVerified = adminToken.isEmailVerified
                    )
                }
        } catch (e: FirebaseAuthException) {
            logger.warn(e.message)
            null
        }
    }
}
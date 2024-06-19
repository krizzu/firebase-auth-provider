package com.kborowy.authprovider.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseApp.DEFAULT_APP_NAME
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.slf4j.LoggerFactory
import java.io.File

internal class FirebaseAdminUtils(adminFile: File) {
    private var app: FirebaseApp? = null
    private val logger = LoggerFactory.getLogger("FirebaseAdmin")

    private val firebaseOptions: FirebaseOptions by lazy {
        FirebaseOptions.builder().run {
            val credentials = GoogleCredentials.fromStream(adminFile.inputStream())
            setCredentials(credentials)
            build()
        }
    }

    fun init() {
        app = initializeFirebaseApp(firebaseOptions)
    }

    fun authenticateToken(token: String): FirebaseToken? {
        requireNotNull(app) { "FirebaseAdminUtils need to be initialized first!" }

        return try {
            FirebaseAuth.getInstance(app).verifyIdToken(token)
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

    private fun initializeFirebaseApp(firebaseOptions: FirebaseOptions): FirebaseApp {
        return try {
            val instance = FirebaseApp.getInstance()
            logger.warn("FirebaseApp name $DEFAULT_APP_NAME already exists!")
            return instance
        } catch (e: IllegalStateException) {
            FirebaseApp.initializeApp(firebaseOptions)
        }
    }
}
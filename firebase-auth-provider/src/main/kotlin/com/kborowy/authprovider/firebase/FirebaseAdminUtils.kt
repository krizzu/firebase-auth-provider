package com.kborowy.authprovider.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
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
        app = FirebaseApp.initializeApp(firebaseOptions)
    }

    fun authenticateToken(token: String): FirebaseToken? {
        if (app == null) {
            throw Exception("FirebaseAdminUtils need to be initialized first!")
        }
        return try {
            FirebaseAuth.getInstance(app).verifyIdToken(token) ?: null
        } catch (e: FirebaseAuthException) {
            logger.warn(e.message)
            null
        }
    }
}
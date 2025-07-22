/*
 * Copyright 2023 Krzysztof Borowy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kborowy.authprovider.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import java.io.InputStream
import org.slf4j.LoggerFactory

internal fun initializeFirebase(
    adminFileStream: InputStream,
    firebaseAppName: String?,
): FirebaseApp {
    val options =
        FirebaseOptions.builder().run {
            val credentials = GoogleCredentials.fromStream(adminFileStream)
            setCredentials(credentials)
            build()
        }

    return if (firebaseAppName != null) FirebaseApp.initializeApp(options, firebaseAppName)
    else FirebaseApp.initializeApp(options)
}

internal suspend fun FirebaseApp.authenticateToken(token: String): FirebaseToken? {
    val logger = LoggerFactory.getLogger("FirebaseAdmin")
    return try {
        FirebaseAuth.getInstance(this).verifyIdTokenAsync(token).await()?.let { adminToken ->
            adminToken.claims
            FirebaseToken(
                uid = adminToken.uid,
                issuer = adminToken.issuer,
                name = adminToken.name,
                picture = adminToken.picture,
                email = adminToken.email,
                claims = adminToken.claims,
                isEmailVerified = adminToken.isEmailVerified,
            )
        }
    } catch (e: FirebaseAuthException) {
        logger.warn(e.message)
        null
    }
}

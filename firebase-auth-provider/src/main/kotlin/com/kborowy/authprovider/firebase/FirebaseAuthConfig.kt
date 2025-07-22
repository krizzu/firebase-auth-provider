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

import com.google.firebase.FirebaseApp
import io.ktor.server.auth.AuthenticationFunction
import io.ktor.server.auth.AuthenticationProvider
import java.io.File
import java.io.InputStream

class FirebaseAuthConfig(name: String?, private val firebaseAppName: String? = null) :
    AuthenticationProvider.Config(name) {
    internal var authenticate: AuthenticationFunction<FirebaseToken> = {
        throw NotImplementedError("Firebase validate function not specified.")
    }

    internal var app: (() -> FirebaseApp) = {
        throw IllegalStateException(
            "Missing Firebase Auth provider setup. Use 'setup' to initialize it."
        )
    }

    var realm: String = "Ktor Server"

    fun setup(block: Setup.() -> Unit) {
        app = { Setup(firebaseAppName).apply(block).getApp() }
    }

    @Deprecated(message = "Use 'setup' instead", level = DeprecationLevel.WARNING)
    var adminFile: File = File("")
        set(file) {
            setup { adminFile = file }
        }

    @Deprecated(message = "Use 'setup' instead", level = DeprecationLevel.WARNING)
    var adminInputStream: InputStream? = null
        set(stream) {
            setup { adminFileStream = stream }
        }

    fun validate(block: AuthenticationFunction<FirebaseToken>) {
        this.authenticate = block
    }

    class Setup(private val firebaseAppName: String?) {
        var firebaseApp: FirebaseApp? = null
        var adminFile: File? = null
        var adminFileStream: InputStream? = null

        internal fun getApp(): FirebaseApp {
            firebaseApp?.let {
                return it
            }

            adminFile?.let {
                return initializeFirebase(it.inputStream(), firebaseAppName)
            }

            adminFileStream?.let {
                return initializeFirebase(it, firebaseAppName)
            }

            error(
                "Incorrect Firebase Auth provider setup - Missing FirebaseApp instance. Provide pre-existing one or service account credential file in 'setup' block."
            )
        }
    }
}

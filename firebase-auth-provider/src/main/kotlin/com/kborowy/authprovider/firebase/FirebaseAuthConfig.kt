/*
 * Copyright 2025 Krzysztof Borowy
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

import io.ktor.server.auth.AuthenticationFunction
import io.ktor.server.auth.AuthenticationProvider
import java.io.File
import java.io.InputStream

class FirebaseAuthConfig(name: String?) : AuthenticationProvider.Config(name) {
    internal lateinit var utils: FirebaseAdminUtils
    internal var authenticate: AuthenticationFunction<FirebaseToken> = {
        throw NotImplementedError("Firebase validate function not specified.")
    }

    var realm: String = "Ktor Server"
    var adminFile: File = File("")
        set(f) {
            if (!::utils.isInitialized) {
                utils = FirebaseAdminUtils(f)
            }
        }

    var adminInputStream: InputStream? = null
        set(f) {
            if (!::utils.isInitialized && f != null) {
                utils = FirebaseAdminUtils(f)
            }
        }

    fun validate(block: AuthenticationFunction<FirebaseToken>) {
        this.authenticate = block
    }
}

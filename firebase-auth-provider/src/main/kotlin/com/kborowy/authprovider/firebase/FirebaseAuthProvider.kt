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
import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.parsing.ParseException
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationFailedCause
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.UnauthorizedResponse
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.response.respond

internal class FirebaseAuthProvider(config: FirebaseAuthConfig) : AuthenticationProvider(config) {

    private val realm = config.realm
    private val authenticate = config.authenticate
    private val app: FirebaseApp = config.app()

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val authHeader =
            try {
                context.call.request.parseAuthorizationHeader()
            } catch (e: ParseException) {
                null
            }
                ?: let {
                    context.challenge(CHALLENGE, AuthenticationFailedCause.NoCredentials) {
                        challenge,
                        call ->
                        call.respond(createUnauthorizedResponse())
                        challenge.complete()
                    }
                    return
                }

        val principal =
            (authHeader as? HttpAuthHeader.Single)
                ?.takeIf { authHeader.authScheme.lowercase() == AuthScheme.Bearer.lowercase() }
                ?.let { app.authenticateToken(token = it.blob) }
                ?.let { authorizedToken -> authenticate(context.call, authorizedToken) }
                ?: let {
                    context.challenge(CHALLENGE, AuthenticationFailedCause.InvalidCredentials) {
                        challenge,
                        call ->
                        call.respond(createUnauthorizedResponse())
                        challenge.complete()
                    }
                    return
                }

        context.principal(principal)
    }

    private fun createUnauthorizedResponse() =
        UnauthorizedResponse(
            HttpAuthHeader.bearerAuthChallenge(scheme = AuthScheme.Bearer, realm = realm)
        )
}

private const val CHALLENGE = "FirebaseAuth"

fun AuthenticationConfig.firebase(name: String? = null, configure: FirebaseAuthConfig.() -> Unit) {
    val config = FirebaseAuthConfig(name).apply(configure)
    val provider = FirebaseAuthProvider(config)
    register(provider)
}

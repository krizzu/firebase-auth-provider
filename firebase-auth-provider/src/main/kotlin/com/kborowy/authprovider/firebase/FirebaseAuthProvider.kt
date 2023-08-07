package com.kborowy.authprovider.firebase

import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.parsing.ParseException
import io.ktor.server.auth.*
import io.ktor.server.response.*

internal class FirebaseAuthProvider(config: FirebaseAuthConfig) : AuthenticationProvider(config) {

    private val realm = config.realm
    private val authenticate = config.authenticate
    private val admin: FirebaseAdminUtils = config.utils


    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val authHeader = try {
            context.call.request.parseAuthorizationHeader()
        } catch (e: ParseException) {
            null
        } ?: let {
            context.challenge(CHALLENGE, AuthenticationFailedCause.NoCredentials) { challenge, call ->
                call.respond(createUnauthorizedResponse())
                challenge.complete()
            }
            return
        }

        val principal = (authHeader as? HttpAuthHeader.Single)
            ?.takeIf { authHeader.authScheme.lowercase() == AuthScheme.Bearer.lowercase() }
            ?.let { admin.authenticateToken(it.blob) }
            ?.let { authorizedToken ->
                authenticate(context.call, authorizedToken)
            }
            ?: let {
                context.challenge(
                    CHALLENGE,
                    AuthenticationFailedCause.InvalidCredentials
                ) { challenge, call ->
                    call.respond(createUnauthorizedResponse())
                    challenge.complete()
                }
                return
            }

        context.principal(principal)
    }

    private fun createUnauthorizedResponse() =
        UnauthorizedResponse(HttpAuthHeader.bearerAuthChallenge(scheme = AuthScheme.Bearer, realm = realm))

}


private const val CHALLENGE = "FirebaseAuth"

fun AuthenticationConfig.firebase(name: String? = null, configure: FirebaseAuthConfig.() -> Unit) {
    val config = FirebaseAuthConfig(name).apply(configure)
    val provider = FirebaseAuthProvider(config)
    register(provider)
}

package com.kborowy.authprovider.firebase

import com.google.firebase.auth.FirebaseToken
import io.ktor.server.auth.*
import java.io.File


class FirebaseAuthConfig(name: String?) : AuthenticationProvider.Config(name) {
    internal lateinit var utils: FirebaseAdminUtils
    internal var authenticate: AuthenticationFunction<FirebaseToken> = {
        throw NotImplementedError("Firebase validate function not specified.")
    }

    var realm: String = "Ktor Server"
    var adminFile: File = File("")
        set(f) {
            if (!::utils.isInitialized) {
                utils = FirebaseAdminUtils(f).apply {
                    init()
                }
            }
        }


    fun validate(block: AuthenticationFunction<FirebaseToken>) {
        this.authenticate = block
    }
}
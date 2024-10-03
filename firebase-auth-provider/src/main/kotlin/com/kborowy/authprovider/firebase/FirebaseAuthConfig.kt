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
package com.samples

import com.kborowy.authprovider.firebase.firebase
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
  embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
      .start(wait = true)
}

fun Application.module() {

  install(CORS) {
    anyHost()
    allowHeader(HttpHeaders.ContentType)
    allowHeader(HttpHeaders.Authorization)
  }
  install(ContentNegotiation) { json() }

  val firebaseAdminResource =
      Thread.currentThread().contextClassLoader.getResourceAsStream("admin.json")

  install(Authentication) {
    firebase("my-auth") {
      adminInputStream = firebaseAdminResource
      realm = "Sample Server"

      validate { token -> UserIdPrincipal(token.uid) }
    }
  }

  val jokes =
      listOf(
          """
            - What's orange and sounds like a parrot
            - Carrot
        """
              .trimIndent(),
          """
            - What do wooden whales eat?
            - Plankton
        """
              .trimIndent(),
          """
            - Why did the scarecrow get a medal?
            - He was outstanding in his field
        """
              .trimIndent())

  routing {
    get("/") { call.respond("Send GET to /joke to get a joke!") }

    authenticate("my-auth") {
      get("joke") {
        val principal = call.principal<UserIdPrincipal>()
        if (principal == null) {
          call.respond(HttpStatusCode.Unauthorized)
          return@get
        }
        val joke = jokes.random()
        call.respond(joke)
      }
    }
  }
}

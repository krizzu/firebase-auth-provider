package com.samples

import com.kborowy.authprovider.firebase.firebase
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

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
    install(ContentNegotiation) {
        json()
    }


    val firebaseAdminFile = File(
        Thread.currentThread().contextClassLoader.getResource("admin.json")?.file
            ?: throw Exception("admin.json file not found in resources")
    )

    install(Authentication) {
        firebase("my-auth") {
            adminFile = firebaseAdminFile
            realm = "Sample Server"

            validate { token ->
                UserIdPrincipal(token.uid)
            }
        }
    }

    val jokes = listOf(
        """
            - What's orange and sounds like a parrot
            - Carrot
        """.trimIndent(),
        """
            - What do wooden whales eat?
            - Plankton
        """.trimIndent(),
        """
            - Why did the scarecrow get a medal? 
            - He was outstanding in his field 
        """.trimIndent()
    )

    routing {
        get("/") {
            call.respond("Send GET to /secret to know a secret!")
        }

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

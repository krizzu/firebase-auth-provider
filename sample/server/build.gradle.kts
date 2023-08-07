
plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    kotlin("plugin.serialization")
}

application {
    mainClass.set("com.samples.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

dependencies {
    implementation(project(":firebase-auth-provider"))
    implementation(libs.bundles.sample.server)
}

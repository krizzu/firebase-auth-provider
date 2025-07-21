plugins {
    kotlin("jvm").version(libs.versions.kotlin.get()).apply(false)
    id("io.ktor.plugin").version(libs.versions.ktor.get()).apply(false)
    kotlin("plugin.serialization").version(libs.versions.kotlin.get()).apply(false)
    id("com.vanniktech.maven.publish").version(libs.versions.publish.get()).apply(false)
    alias(libs.plugins.validator.abi) apply false
    alias(libs.plugins.spotless)
}

spotless {
    val buildDirectory = layout.buildDirectory.asFileTree
    val licenseFile = rootProject.file("spotless/license.kt")
    kotlin {
        target("**/*.kt", "**/*.kts")
        ktfmt().kotlinlangStyle()
        targetExclude(buildDirectory, licenseFile, "**/*.gradle.kts", "sample/**/*.kt")
        licenseHeaderFile(licenseFile)
        trimTrailingWhitespace()
        endWithNewline()
    }
}


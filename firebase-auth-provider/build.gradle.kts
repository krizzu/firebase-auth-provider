import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
}

kotlin {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_1_9)
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    implementation(libs.firebase.admin) {
        exclude("com.google.guava")
    }
    implementation(libs.firebase.admin.guavaWorkaround)
    implementation(libs.ktor.auth)
    implementation(libs.logger)
}
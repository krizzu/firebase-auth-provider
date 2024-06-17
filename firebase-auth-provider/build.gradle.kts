plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish")
}

dependencies {
    implementation(libs.firebase.admin) {
        exclude("com.google.guava")
    }
    implementation(libs.firebase.admin.guavaWorkaround)
    implementation(libs.ktor.auth)
    implementation(libs.logger)
}
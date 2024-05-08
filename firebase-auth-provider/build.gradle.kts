plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish").version(libs.versions.publish.get())
}

dependencies {
    implementation(libs.firebase.admin)
    implementation(libs.ktor.auth)
    implementation(libs.logger)
}
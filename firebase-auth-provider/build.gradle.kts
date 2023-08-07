import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish").version(libs.versions.publish.get().toString())
}

dependencies {
    api(libs.firebase.admin)
    implementation(libs.ktor.auth)
    implementation(libs.logger)
}
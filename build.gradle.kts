import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "1.9.23"

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    val voyagerVersion = "1.1.0-beta02"

    //implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("cafe.adriel.voyager:voyager-navigator:${voyagerVersion}")
    implementation("cafe.adriel.voyager:voyager-transitions:${voyagerVersion}")


    // Retrofit core (solo funciona en Android y JVM)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter (solo Android y JVM)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ClienteParaProbarWebSoket"
            packageVersion = "1.0.0"
        }
    }
}

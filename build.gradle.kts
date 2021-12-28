import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)

    val exposedVersion: String by project
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("mysql:mysql-connector-java:5.1.48")
}

compose.desktop {
    application {
        mainClass = "com.zzz.MainKt"
    }
}

plugins {
    kotlin("jvm") version "2.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.telegram:telegrambots:6.3.0")
    implementation("org.telegram:telegrambots-abilities:6.3.0")
    implementation("org.telegram:telegrambotsextensions:6.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
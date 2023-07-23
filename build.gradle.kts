plugins {
    kotlin("jvm") version "1.8.20"
    application
}

group = "me.farva"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}
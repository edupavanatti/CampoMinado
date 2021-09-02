import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}

group = "me.eduardo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
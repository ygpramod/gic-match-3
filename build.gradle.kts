plugins {
    kotlin("jvm") version "2.3.10"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.test {
        useJUnitPlatform()
    }
}
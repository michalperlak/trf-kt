import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
}

group = "pl.michalperlak"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

val arrowVersion = "0.10.4"
val junitVersion = "5.6.0"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation ("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testRuntimeOnly ("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
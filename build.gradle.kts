import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val kotlinVersion = "1.9.20"
    kotlin("jvm") version kotlinVersion // Kotlin JVM을 사용하는 프로젝트를 위한 플러그인
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" // Kotlin 코드 스타일을 자동으로 검사하고 포맷팅하는 도구
}

group = "com"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val springmockk = project.findProperty("springmockk")
val kotestRunner = project.findProperty("kotest.runner.junit5")
val kotestAssertions = project.findProperty("kotest.assertions")
val kotestExtensions = project.findProperty("kotest-extensions")
val jsoup = project.findProperty("jsoup")
val kotlinLogging = project.findProperty("kotlin.logging")

dependencies {
    // web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // data
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Utility libraries
    implementation("org.jsoup:jsoup:$jsoup")
    implementation("org.apache.commons:commons-lang3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLogging")

    // Spring Boot Devtools
    compileOnly("org.springframework.boot:spring-boot-devtools")

    // Testing libraries
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "org.mockito")
    }
    testImplementation("com.ninja-squad:springmockk:$springmockk")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestRunner")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestExtensions")
    testImplementation("io.kotest:kotest-assertions-core:$kotestAssertions")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

import io.gatling.gradle.LogHttp

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.webjars.npm:htmx.org:1.9.12")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.github.ua-parser:uap-java:1.6.1")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    runtimeOnly("org.postgresql:postgresql")


    // Gatling
    gatlingImplementation("com.google.guava:guava:33.1.0-jre")
    gatlingImplementation("org.apache.commons:commons-lang3:3.14.0")
    gatlingCompileOnly("org.projectlombok:lombok")
    gatlingAnnotationProcessor("org.projectlombok:lombok")

    // Subprojects
    implementation(project(":mail-service"))
}

plugins {
    id("io.gatling.gradle") version "3.9.5.1"
}

gatling {
    gatlingVersion = "3.10.5"
    logLevel = "DEBUG"
    logHttp = LogHttp.NONE
    enterprise.closureOf<Any> {
    }
}
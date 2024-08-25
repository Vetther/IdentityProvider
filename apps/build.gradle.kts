import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val springCloudVersion by extra { "2023.0.3" }
val springBootVersion by extra { "3.3.2" }

plugins {
    java
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.5"
//    id("org.graalvm.buildtools.native") version "0.10.2"
}

allprojects {
    group = "pl.owolny"
    version = "1.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
//    apply(plugin = "org.graalvm.buildtools.native")

    java.sourceCompatibility = JavaVersion.VERSION_21

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        implementation("org.mapstruct:mapstruct:1.5.5.Final")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
        implementation("org.springframework.boot:spring-boot-starter-amqp")

        // Observability
        implementation("com.github.loki4j:loki-logback-appender:1.6.0-m1")
        implementation("io.micrometer:micrometer-registry-prometheus")
        implementation("io.micrometer:micrometer-tracing-bridge-brave")
        implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    tasks.withType<BootBuildImage> {
        imageName = "vetther/${rootProject.name}-${project.name}"
        buildpacks = listOf("gcr.io/paketo-buildpacks/eclipse-openj9:latest", "paketo-buildpacks/java")
    }

    configurations {
        create("providedRuntime")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
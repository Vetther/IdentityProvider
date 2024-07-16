import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

val springCloudVersion by extra { "2023.0.3" }
val springBootVersion by extra { "3.3.1" }

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

allprojects {
    group = "pl.owolny"
    version = "1.0"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    java.sourceCompatibility = JavaVersion.VERSION_21

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    tasks.withType<BootBuildImage> {
        imageName = "owolny/${rootProject.name}-${project.name}"
    }

    configurations {
        create("providedRuntime")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
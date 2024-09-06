import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

dependencies {
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
}

tasks.withType<BootBuildImage> {
	imageName = "vetther/${rootProject.name}-${project.name}"

	environment.put("BP_JVM_CDS_ENABLED", "false")
	environment.put("BP_SPRING_AOT_ENABLED", "false")

	buildpacks = listOf("gcr.io/paketo-buildpacks/eclipse-openj9:latest", "paketo-buildpacks/java")
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.5.10"
	id("org.springframework.boot") version "2.7.16"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.openapi.generator") version "7.1.0"
}

group = "com.wanderers"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
	implementation("io.swagger.core.v3:swagger-models:2.2.8")
	implementation("org.springdoc:springdoc-openapi-ui:1.7.0")

	implementation("org.postgresql:postgresql:42.3.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

openApiGenerate {
	generatorName.set("kotlin-spring")
	validateSpec.set(true)
	inputSpec.set("$projectDir/src/main/resources/static/hotelier-webservices-v1.yaml")
	outputDir.set("$buildDir/generated/openapi")
	apiPackage.set("com.wanderers.hotelier_webservices.rest.api")
	modelPackage.set("com.wanderers.hotelier_webservices.rest.model")

	configOptions.put("dateLibrary", "java8")
	configOptions.put("hideGenerationTimestamp", "true")
	configOptions.put("delegatePattern", "true")
	configOptions.put("library", "spring-boot")
	configOptions.put("useBeanValidation", "true")
	configOptions.put("useTags", "true")
	configOptions.put("implicitHeaders", "true")
	configOptions.put("openApiNullable", "false")
}

kotlin.sourceSets["main"].kotlin.srcDirs("$buildDir/generated/openapi/src/main/kotlin")

tasks.withType<KotlinCompile> {
	dependsOn(tasks.named("openApiGenerate"))
	kotlinOptions.jvmTarget = "11"
}

tasks.register<Zip>("buildZip") {
	from(tasks.named("compileKotlin"))
	from(tasks.named("processResources"))
	into("lib") {
		from(configurations["compileClasspath"])
	}
}

tasks.named("build") {
	dependsOn(tasks.named("buildZip"))
}

springBoot {
	mainClass.set("com.wanderers.hotelier_webservices.HotelierWebservicesApplication")
}

tasks.test {
	useJUnitPlatform()
}

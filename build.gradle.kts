plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.crm"
version = "0.0.1-SNAPSHOT"
description = "Users API"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Reactive Web
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// Reactive database access
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.postgresql:r2dbc-postgresql:1.0.4.RELEASE")

	//	Lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Optional: Reactive validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}

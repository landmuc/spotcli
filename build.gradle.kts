plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "com.landmuc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

extra["springShellVersion"] = "3.4.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.shell:spring-shell-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
//implementation("org.springframework.boot:spring-boot-starter-security")

	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.shell:spring-shell-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.shell:spring-shell-dependencies:${property("springShellVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

plugins {
	java
	id("org.springframework.boot") version "3.0.3"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "jerik.the.dog"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_19

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// https://jwt.io/libraries?language=Java
	// https://github.com/jwtk/jjwt#dependencies

	// jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5") // creates and validates JWTs
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5") // creates and validates JWTs
	// Unable to find an implementation for interface io.jsonwebtoken.io.Serializer using java.util.ServiceLoader.
	// Ensure you include a backing implementation .jar in the classpath, for example jjwt-impl.jar, or your own .jar for custom implementations.
	runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.11.5")

	implementation("javax.xml.bind:jaxb-api:2.3.1") // supposedly needed, we'll see about that

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

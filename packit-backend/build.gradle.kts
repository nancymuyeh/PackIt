plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("org.graalvm.buildtools.native") version "0.10.2"
	id("org.openapi.generator") version "6.6.0"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "com.packit"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework:spring-jdbc")
	implementation("io.swagger:swagger-annotations:1.6.11")
    implementation(kotlin("stdlib"))
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.2.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("io.r2dbc:r2dbc-h2")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.packit.api")
    modelPackage.set("com.packit.model")
    invokerPackage.set("com.packit.invoker")
    configOptions.set(mapOf(
        "dateLibrary" to "java8",
        "useTags" to "true",
        "interfaceOnly" to "false",
        "reactive" to "true",
        "useBeanValidation" to "true",
        "exceptionHandler" to "true",
        "serviceInterface" to "true",
        "useSpringAnnotation" to "true",
        "serializationLibrary" to "jackson"
    ))
}

tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateApi") {
    dependsOn("clean")
    doLast {
        println("OpenAPI code generation complete!")
    }
}

tasks.named("compileKotlin") {
    dependsOn("openApiGenerate")
}


kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

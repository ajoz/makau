import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.71"
    `java-library`
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // aggregate jqwik dependency
    testImplementation("net.jqwik:jqwik:1.5.0")

    // Add if you also want to use the Jupiter engine or Assertions from it
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")

    // Add any other test library you need...
    testImplementation("org.assertj:assertj-core:3.12.2")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform() {
        includeEngines(
                "jqwik",
                "junit-jupiter"
        )
    }

    include(
            "**/*Properties.class",
            "**/*Test.class",
            "**/*Tests.class"
    )
}

// compile bytecode to Java 8 (default is Java 6)
// jqwik API is using static interface methods that are not usable in Kotlin
// if Kotlin is set to Java 1.6
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
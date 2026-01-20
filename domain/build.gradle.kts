import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.testing
import kotlin.apply

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = ProjectConfig.javaVersion
    targetCompatibility = ProjectConfig.javaVersion
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(ProjectConfig.jvmVersion)
    }
    tasks.test {
        useJUnitPlatform()

        // Apply your reporting and logging here
        testLogging {
            events(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
            )
        }

        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}

tasks.register("kotlinUnitTests") {
    group = "verification"
    description = "Runs unit tests for pure Kotlin modules"
    dependsOn(tasks.withType<Test>())
}


dependencies {
    // Dagger
    implementation(libs.dagger)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(kotlin("test"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.bundles.testing.junit)
}

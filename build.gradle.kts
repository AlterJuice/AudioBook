import org.gradle.api.tasks.testing.logging.TestLogEvent

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.hilt.android) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

    tasks.withType<Test>().configureEach {
        // 1. Use the JUnit 5 engine
        useJUnitPlatform()

        // 2. Configure console logging
        testLogging {
            events = setOf(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED,
                TestLogEvent.STANDARD_OUT,
                TestLogEvent.STANDARD_ERROR,
            )
            showStandardStreams = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }

        // 3. Configure Report generation
        reports {
            junitXml.required.set(true) // Required for GitHub Actions / Jenkins
            html.required.set(true)     // Viewable in your browser
        }

    }
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
    }

}
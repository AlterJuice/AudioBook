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
}

dependencies {
    // Dagger
    implementation(libs.dagger)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)
}

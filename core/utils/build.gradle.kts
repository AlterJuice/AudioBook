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
    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
}
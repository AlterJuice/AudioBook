plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.alterjuice.test.audiobook"
    compileSdk = ProjectConfig.compileSdk
    lint {
        ignoreTestSources = true
        // Generate plain text, HTML, and XML reports for CI artifacts

        xmlReport = true
        xmlOutput = file("${layout.buildDirectory}/reports/lint-results-aggregated.xml")

        htmlReport = true
        htmlOutput = file("${layout.buildDirectory}/reports/lint-results-aggregated.html")


        // Ensure all dependencies are also checked for issues
        checkDependencies = true

        // Promote MissingTranslation from a warning to a fatal error
        // fatal.addAll(listOf("MissingTranslation", "MissingSealedEntries"))

        // Fail the build if any issue with severity 'error' or 'fatal' is found
        abortOnError = true
    }
    defaultConfig {
        applicationId = "com.alterjuice.test.audiobook"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmVersion
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Media3
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)

    // Str lib
    implementation(libs.alterjuice.str.core)
    implementation(libs.alterjuice.str.android)

    // Project modules
    implementation(project(":feature:book-player"))
    implementation(project(":core:ui"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":errors"))
    implementation(project(":core:utils"))


    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
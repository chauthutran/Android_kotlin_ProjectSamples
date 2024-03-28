plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.psi.fhir"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.psi.fhir"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures.buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }

//    configurations {
//        all {
//            exclude("com.sun.activation:javax.activation:1.2.0", "javax.activation-1.2.0" )
//        }
//    }

//    configurations.all {
//        resolutionStrategy {
//            force("com.fasterxml.jackson.core:jackson-core:2.12.5")
//            // Force the version of jackson-datatype-jsr310
//            force("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.5")
//
//            // Force the version of jackson-databind
//            force("com.fasterxml.jackson.core:jackson-databind:2.12.5")
//            force("com.fasterxml.jackson:jackson-bom:2.12.5")
//        }
//    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
//    implementation("androidx.navigation:navigation-compose:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    implementation("com.google.android.fhir:engine:1.0.0")
//    implementation("com.google.android.fhir:data-capture:1.1.0")
    implementation("com.google.android.fhir:engine:0.1.0-beta05")
    implementation("com.google.android.fhir:data-capture:1.0.0")

//    implementation ("com.google.http-client:google-http-client:1.39.2")
//    implementation("com.google.http-client:google-http-client-gson:1.39.2")
//    implementation ("com.google.apis:google-api-services-healthcare:v1-rev20211021-1.32.1")



    implementation("androidx.fragment:fragment-ktx:1.6.2")


    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${rootProject.extra["lifecycle_version"]}")

    implementation("androidx.navigation:navigation-compose:2.7.4")

//    implementation("com.fasterxml.jackson.core:jackson-core:2.12.5")

//    implementation("com.squareup.okhttp3:okhttp:4.9.2")
}
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("io.realm.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

//    id("plugin.serialization")

//    id("plugin.serialization").version("1.8.0")

//    id 'org.jetbrains.kotlin.plugin.serialization' version '1.8.0'
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.psi.onlineshop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.psi.onlineshop"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "META-INF/native-image/org.mongodb/bson/native-image.properties"
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }

    }



}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.android.material:material:1.4.0")
//    implementation("com.google.firebase:firebase-firestore:24.10.1")
//    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


//    apply plugin :"kotlin-kapt"

    //Navigation component
    val nav_version = "2.7.6"

    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //loading button
    implementation("br.com.simplepass:loading-button-android:2.2.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.13.0")

    //circular image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //viewpager2 indicatior
    implementation("io.github.vejei.viewpagerindicator:viewpagerindicator:1.0.0-alpha.1")

    //stepView
    implementation("com.shuhart.stepview:stepview:1.5.1")

    //Android Ktx
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")

    //Dagger hilt
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-compiler:2.50")

//    //Firebase
//    implementation("com.google.firebase:firebase-auth:22.3.1")

//    //Coroutines with firebase
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.1")


    //Color picker
    implementation("com.github.skydoves:colorpickerview:2.2.4")

//    // Mongodb
////    implementation ("org.mongodb:mongodb-driver-kotlin:1.7.2")
////    // // https://mvnrepository.com/artifact/org.mongodb/mongodb-driver-kotlin-sync
////    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.0.0")
//
//    // Kotlin coroutine dependency
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//
//    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")

    // Kotlin coroutine dependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//
//    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1") {
//        exclude(group = "org.mongodb", module = "bson-record-codec")
//    }
//    implementation("ch.qos.logback:logback-classic:1.2.11")
//
////    testImplementation(kotlin("test"))
//////    implementation("org.mongodb:bson-kotlinx:4.11.0")
//
//    // Mongo Realm
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//    implementation("io.realm.kotlin:library-sync:1.11.0")// If using Device Sync
////    implementation ("io.realm.kotlin:library-base:1.6.1")

//    implementation("com.squareup.okhttp3:okhttp:4.9.0")

//    implementation("io.github.rybalkinsd:kohttp:0.12.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("com.google.code.gson:gson:2.8.5")
}


kapt {
    correctErrorTypes=true
}
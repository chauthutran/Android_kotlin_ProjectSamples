import Build_gradle.Dependencies.forceGuava
import Build_gradle.Dependencies.forceHapiVersion
import Build_gradle.Dependencies.forceJacksonVersion
import Build_gradle.Dependencies.removeIncompatibleDependencies
//import Build_gradle.Dependencies.forceCqlVersion

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
        resources.excludes.addAll(
            listOf(
                "license.html",
                "readme.html",
                "META-INF/*",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/license.html",
                "META-INF/LICENSE.md",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/NOTICE.md",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/ASL-2.0.txt",
                "META-INF/LGPL-3.0.txt",
                "META-INF/sun-jaxb.episode",
                "META-INF/*.kotlin_module",
                "META-INF/INDEX.LIST",
                "xsd/catalog.xml",
                "plugin.xml"
            ),
        )


//        resources {
//            excludes += "META-INF/*"
//
//        }
    }

    configurations {
        all {
            exclude("com.sun.activation:javax.activation:1.2.0", "javax.activation-1.2.0", )
            exclude("com.sun.activation", "jakarta.activation" )
            exclude("jakarta.activation", "jakarta.activation-api" )

        }
    }

}


dependencies {

//    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.compose.animation:animation-graphics-android:1.6.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")



    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${rootProject.extra["lifecycle_version"]}")

    implementation("androidx.navigation:navigation-compose:2.7.4")

    implementation ("io.apisense:rhino-android:1.0")

    // To convert data class object to json
    implementation("com.google.code.gson:gson:2.8.5")

    // kotlinx-coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    // Snackbar
    implementation("androidx.compose.material:material:1.0.5")

////    // To convert json object to FHIR Resource object
//    implementation ("ca.uhn.hapi.fhir:hapi-fhir-base:5.6.2")

    // Generate StructureMap in unit test
    // https://mvnrepository.com/artifact/ca.uhn.hapi.fhir/hapi-fhir-structures-r4
//    implementation("ca.uhn.hapi.fhir:hapi-fhir-structures-r4:7.0.2")
    implementation("javax.inject:javax.inject:1")

    implementation("com.jakewharton.timber:timber:5.0.1")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")


     val clinicalReasoning = "3.0.0-PRE9"
    implementation("org.opencds.cqf.fhir:cqf-fhir-cr:${clinicalReasoning}")
    implementation("org.opencds.cqf.fhir:cqf-fhir-jackson:${clinicalReasoning}")
    implementation("org.opencds.cqf.fhir:cqf-fhir-utility:${clinicalReasoning}")


    implementation("com.google.android.fhir:common:0.1.0-alpha05")
    implementation("com.google.android.fhir:engine:1.0.0")
    implementation("com.google.android.fhir:data-capture:1.1.0")
    implementation("com.google.android.fhir:knowledge:0.1.0-alpha03")
    implementation("com.google.android.fhir:workflow:0.1.0-alpha04")
//    implementation("com.google.android.fhir:contrib-barcode:0.1.0-beta3")
//    implementation("com.google.android.fhir:contrib-locationwidget:0.1.0-alpha01")


//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.library
//    implementation("org.opencds.cqf.cql:evaluator.library:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.plandefinition
//    implementation("org.opencds.cqf.cql:evaluator.plandefinition:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.measure
//    implementation("org.opencds.cqf.cql:evaluator.measure:3.0.0-PRE5")
//    implementation("org.opencds.cqf.cql:evaluator.measure.r4:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.activitydefinition
//    implementation("org.opencds.cqf.cql:evaluator.activitydefinition:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.expression
//    implementation("org.opencds.cqf.cql:evaluator.expression:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.measure-hapi
//    implementation("org.opencds.cqf.cql:evaluator.measure-hapi:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator.fhir
//    implementation("org.opencds.cqf.cql:evaluator.fhir:3.0.0-PRE5")
//// https://mvnrepository.com/artifact/org.opencds.cqf.cql/evaluator
//    implementation("org.opencds.cqf.cql:evaluator:3.0.0-PRE5")
//    implementation("org.opencds.cqf.cql:evaluator.builder:3.0.0-PRE5")


//    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.22.0")
//    implementation("com.android.tools.build:gradle:8.1.4")
//    implementation("app.cash.licensee:licensee-gradle-plugin:1.8.0")
//    implementation("com.osacky.flank.gradle:fladle:0.17.4")
//    implementation("com.spotify.ruler:ruler-gradle-plugin:1.4.0")
//    implementation("ca.uhn.hapi.fhir:hapi-fhir-structures-r4:6.10.0")
//    implementation("com.squareup:kotlinpoet:1.15.3")

//    implementation ("javax.xml:javax.xml.stream-api:1.0")

}


configurations {
    all {
        exclude(group = "org.eclipse.persistence")
        exclude(group = "javax.activation", module = "activation")
        exclude(group = "javax", module = "javaee-api")
//        exclude(group = "xml-apis")
        exclude(group = "xpp3")

        removeIncompatibleDependencies()
        forceGuava()
        forceHapiVersion()
        forceJacksonVersion()
//        forceCqlVersion()
    }
}


/**
 * workflow library strictly depends on the versions of the dependencies defined here.
 * [Configuration.force**] functions defined here force gradle to pick correct versions of the
 * dependencies as required by wprkflow.
 */
object Dependencies {
    const val guava = "com.google.guava:guava:${Versions.guava}"
    object HapiFhir {
        const val fhirBase = "ca.uhn.hapi.fhir:hapi-fhir-base:${Versions.hapiFhir}"
        const val fhirClient = "ca.uhn.hapi.fhir:hapi-fhir-client:${Versions.hapiFhir}"
        const val structuresDstu2 = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu2:${Versions.hapiFhir}"
        const val structuresDstu3 = "ca.uhn.hapi.fhir:hapi-fhir-structures-dstu3:${Versions.hapiFhir}"
        const val structuresR4 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4:${Versions.hapiFhir}"
        const val structuresR4bModule = "ca.uhn.hapi.fhir:hapi-fhir-structures-r4b:${Versions.hapiFhir}"
        const val structuresR5 = "ca.uhn.hapi.fhir:hapi-fhir-structures-r5:${Versions.hapiFhir}"

        const val validation = "ca.uhn.hapi.fhir:hapi-fhir-validation:${Versions.hapiFhir}"
        const val validationDstu3 =
            "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-dstu3:${Versions.hapiFhir}"
        const val validationR4 =
            "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r4:${Versions.hapiFhir}"
        const val validationR5 =
            "ca.uhn.hapi.fhir:hapi-fhir-validation-resources-r5:${Versions.hapiFhir}"

        const val fhirCoreDstu2 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2:${Versions.hapiFhirCore}"
        const val fhirCoreDstu2016 =
            "ca.uhn.hapi.fhir:org.hl7.fhir.dstu2016may:${Versions.hapiFhirCore}"
        const val fhirCoreDstu3 = "ca.uhn.hapi.fhir:org.hl7.fhir.dstu3:${Versions.hapiFhirCore}"
        const val fhirCoreR4 = "ca.uhn.hapi.fhir:org.hl7.fhir.r4:${Versions.hapiFhirCore}"
        const val fhirCoreR4b = "ca.uhn.hapi.fhir:org.hl7.fhir.r4b:${Versions.hapiFhirCore}"
        const val fhirCoreR5 = "ca.uhn.hapi.fhir:org.hl7.fhir.r5:${Versions.hapiFhirCore}"
        const val fhirCoreUtils = "ca.uhn.hapi.fhir:org.hl7.fhir.utilities:${Versions.hapiFhirCore}"
        const val fhirCoreConvertors =
            "ca.uhn.hapi.fhir:org.hl7.fhir.convertors:${Versions.hapiFhirCore}"

        const val guavaCaching = "ca.uhn.hapi.fhir:hapi-fhir-caching-guava:${Versions.hapiFhir}"
    }

    object Jackson {
        private const val mainGroup = "com.fasterxml.jackson"
        private const val coreGroup = "$mainGroup.core"
        private const val dataformatGroup = "$mainGroup.dataformat"
        private const val datatypeGroup = "$mainGroup.datatype"
        private const val moduleGroup = "$mainGroup.module"

        const val annotations = "$coreGroup:jackson-annotations:${Versions.jackson}"
        const val bom = "$mainGroup:jackson-bom:${Versions.jackson}"
        const val core = "$coreGroup:jackson-core:${Versions.jacksonCore}"
        const val databind = "$coreGroup:jackson-databind:${Versions.jackson}"
        const val dataformatXml = "$dataformatGroup:jackson-dataformat-xml:${Versions.jackson}"
        const val jaxbAnnotations = "$moduleGroup:jackson-module-jaxb-annotations:${Versions.jackson}"
        const val jsr310 = "$datatypeGroup:jackson-datatype-jsr310:${Versions.jackson}"
    }
    object Versions {
        object Cql {
            const val clinicalReasoning = "3.0.0-PRE9-SNAPSHOT"
        }
        const val guava = "32.1.2-android"
        const val hapiFhir = "6.8.0"
        const val hapiFhirCore = "6.0.22"
        // Maximum Jackson libraries (excluding core) version that supports Android API Level 24:
        // https://github.com/FasterXML/jackson-databind/issues/3658
        const val jackson = "2.13.5"
        // Maximum Jackson Core library version that supports Android API Level 24:
        const val jacksonCore = "2.15.2"
    }

    fun Configuration.removeIncompatibleDependencies() {
        exclude(module = "xpp3")
        exclude(module = "xpp3_min")
        exclude(module = "xmlpull")
        exclude(module = "javax.json")
        exclude(module = "jcl-over-slf4j")
        exclude(group = "org.apache.httpcomponents")
        exclude(group = "org.antlr", module = "antlr4")
        exclude(group = "org.eclipse.persistence", module = "org.eclipse.persistence.moxy")

        exclude(module = "hapi-fhir-caching-caffeine")
        exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")
    }

    fun Configuration.forceGuava() {
        // Removes caffeine
        exclude(module = "hapi-fhir-caching-caffeine")
        exclude(group = "com.github.ben-manes.caffeine", module = "caffeine")

        resolutionStrategy {
            force(guava)
            force(HapiFhir.guavaCaching)
        }
    }

    fun Configuration.forceHapiVersion() {
        // Removes newer versions of caffeine and manually imports 2.9
        // Removes newer versions of hapi and keeps on 6.0.1
        // (newer versions don't work on Android)
        resolutionStrategy {
            force(HapiFhir.fhirBase)
            force(HapiFhir.fhirClient)
            force(HapiFhir.fhirCoreConvertors)

            force(HapiFhir.fhirCoreDstu2)
            force(HapiFhir.fhirCoreDstu2016)
            force(HapiFhir.fhirCoreDstu3)
            force(HapiFhir.fhirCoreR4)
            force(HapiFhir.fhirCoreR4b)
            force(HapiFhir.fhirCoreR5)
            force(HapiFhir.fhirCoreUtils)

            force(HapiFhir.structuresDstu2)
            force(HapiFhir.structuresDstu3)
            force(HapiFhir.structuresR4)
            force(HapiFhir.structuresR4bModule)
            force(HapiFhir.structuresR5)

            force(HapiFhir.validation)
            force(HapiFhir.validationDstu3)
            force(HapiFhir.validationR4)
            force(HapiFhir.validationR5)
        }
    }

    fun Configuration.forceJacksonVersion() {
        resolutionStrategy {
            force(Jackson.annotations)
            force(Jackson.bom)
            force(Jackson.core)
            force(Jackson.databind)
            force(Jackson.jaxbAnnotations)
            force(Jackson.jsr310)
            force(Jackson.dataformatXml)
        }
    }

//    fun Configuration.forceCqlVersion() {
//        resolutionStrategy {
//            force(Versions.Cql)
//        }
//    }
}
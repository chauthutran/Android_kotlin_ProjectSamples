
plugins {
    `kotlin-dsl`
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    extra.apply {
        set("lifecycle_version", "2.6.2")
    }
//    repositories {
//        google()
////        jcenter()
//        mavenCentral()
//////        maven(url = "https://example.com/repository/maven-public/")
////        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
//        gradlePluginPortal()
//    }


    dependencies {
        classpath("com.android.tools.build:gradle:8.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
//        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.6.0")
    }
}


//allprojects {
////    repositories {
////        google()
////        mavenCentral()
////        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
////        gradlePluginPortal()
////    }
//    configureSpotless()
//}


allprojects {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        google()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://jcenter.bintray.com/")
//        apply(plugin = "org.owasp.dependencycheck")
//        tasks.dependencyCheckAggregate{
//            dependencyCheck.formats.add("XML")
//        }
    }
    configureSpotless()
}

fun Project.configureSpotless() {
    val ktlintVersion = "0.41.0"
    val ktlintOptions = mapOf("indent_size" to "2", "continuation_indent_size" to "2")
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        ratchetFrom = "origin/main"
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/")
            targetExclude("**/*_Generated.kt")
            ktlint(ktlintVersion).userData(ktlintOptions)
            ktfmt().googleStyle()
            licenseHeaderFile(
                "${project.rootProject.projectDir}/license-header.txt",
                "package|import|class|object|sealed|open|interface|abstract "
                // It is necessary to tell spotless the top level of a file in order to apply config to it
                // See: https://github.com/diffplug/spotless/issues/135
            )
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(ktlintVersion).userData(ktlintOptions)
            ktfmt().googleStyle()
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/", ".idea/")
            prettier(mapOf("prettier" to "2.0.5", "@prettier/plugin-xml" to "0.13.0"))
                .config(mapOf("parser" to "xml", "tabWidth" to 4))
        }
        // Creates one off SpotlessApply task for generated files
        com.diffplug.gradle.spotless.KotlinExtension(this).apply {
            target("**/*_Generated.kt")
            ktlint(ktlintVersion).userData(ktlintOptions)
            ktfmt().googleStyle()
            licenseHeaderFile(
                "${project.rootProject.projectDir}/license-header.txt",
                "package|import|class|object|sealed|open|interface|abstract "
            )
            createIndependentApplyTask("spotlessGenerated")
        }
    }
}
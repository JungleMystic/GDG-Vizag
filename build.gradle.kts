// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    //For SafeArgs
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("com.android.tools.build:gradle:3.4.3")
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.8")
    }
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id ("com.google.devtools.ksp") version "1.6.10-1.0.2" apply false
}
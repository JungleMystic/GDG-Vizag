plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // for databinding
    id ("kotlin-kapt")
    id ("kotlin-android")

    // SafeArgs
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
    id ("com.google.devtools.ksp")

    id ("kotlin-parcelize")
}

android {
    namespace = "com.lrm.gdgvizag"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lrm.gdgvizag"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Navigation Component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.0")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.6.1")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // glide for images
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    ksp("com.github.bumptech.glide:ksp:4.14.2")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //Lifecycle extensions
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    // WorkManager dependency
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    //Easy Permissions
    implementation ("com.vmadalin:easypermissions-ktx:1.0.0")

    //Facebook shimmer effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    //Lottie Animation
    implementation ("com.airbnb.android:lottie:6.1.0")

    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    //Firebase Authentication
    implementation("com.google.firebase:firebase-auth-ktx")

    // Dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.6.0")

    // Firebase Cloud Firestore
    implementation ("com.google.firebase:firebase-firestore-ktx")

    // Firebase Storage
    implementation("com.google.firebase:firebase-storage-ktx")

    // Firebase Crashlytics
    implementation ("com.google.firebase:firebase-crashlytics-ktx")
    implementation ("com.google.firebase:firebase-analytics-ktx")

    // Circle image view library
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Location API
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    //SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
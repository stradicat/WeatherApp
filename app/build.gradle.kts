plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "dev.dmayr.weatherapp"
    //noinspection GradleDependency
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.dmayr.weatherapp"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            buildConfigField("String", "WEATHER_API_KEY", "\"afc5548d84ae2ca875d7ac35945cd7c3\"")
        }
        debug {
            buildConfigField("String", "WEATHER_API_KEY", "\"afc5548d84ae2ca875d7ac35945cd7c3\"")
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Retrofit para API calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Location Services
    implementation(libs.play.services.location)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // RecyclerView
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Display seguro de Emojis como `drawable`
    implementation(libs.androidx.emoji2)
    implementation(libs.androidx.emoji2.views)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

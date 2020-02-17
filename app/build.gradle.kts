import com.android.build.gradle.internal.dsl.BaseFlavor
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.jetbrains.kotlin.config.AnalysisFlags.experimental
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(AppConfig.COMPILE_SDK_VERSION)
    dataBinding.isEnabled = true
    defaultConfig {
        applicationId = AppConfig.ID
        minSdkVersion(AppConfig.MIN_SDK_VERSION)
        targetSdkVersion(AppConfig.TARGET_SDK_VERSION)
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME
        testInstrumentationRunner = AppConfig.TEST_INSTRUMENTATION_RUNNER
        vectorDrawables.useSupportLibrary = AppConfig.SUPPORT_LIBRARY_VECTOR_DRAWABLES

        buildConfigFieldFromGradleProperty("currencyApiKey")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
        // "this" is currently lacking a proper type
        // See: https://youtrack.jetbrains.com/issue/KT-31077
        val options = this as? KotlinJvmOptions
        options?.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    kapt("androidx.room:room-compiler:${Versions.roomVersion}")
    implementation("androidx.room:room-runtime:${Versions.roomVersion}")
    implementation("androidx.room:room-rxjava2:${Versions.roomVersion}")
    implementation("androidx.room:room-ktx:${Versions.roomVersion}")

    implementation(LibraryDependencies.NAVIGATION_FRAGMENT)
    implementation(LibraryDependencies.NAVIGATION_UI_KTX)
    implementation("androidx.appcompat:appcompat:${Versions.supportLibraryVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}")
    implementation("androidx.core:core-ktx:${Versions.ktxVersion}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleVersion}")
    implementation("androidx.recyclerview:recyclerview:${Versions.recyclerViewVersion}")
    implementation("com.github.bumptech.glide:glide:${Versions.glideVersion}")
    kapt("com.github.bumptech.glide:compiler:${Versions.glideVersion}")
    implementation("com.google.android.material:material:${Versions.materialVersion}")
    implementation("com.google.code.gson:gson:${Versions.gsonVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
    implementation(LibraryDependencies.KOTLINX_COROUTINES)
    implementation(LibraryDependencies.KOTLINX_COROUTINES_ANDROID)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-beta01")

// Testing dependencies
    androidTestImplementation("androidx.arch.core:core-testing:${Versions.coreTestingVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${Versions.espressoVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.espressoVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${Versions.espressoVersion}")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:${Versions.uiAutomatorVersion}")
    testImplementation("junit:junit:${Versions.junitVersion}")

    implementation("com.google.dagger:dagger:${Versions.daggerVersion}")
    kapt("com.google.dagger:dagger-compiler:${Versions.daggerVersion}")
    kapt("com.google.dagger:dagger-android-processor:${Versions.daggerVersion}")
    implementation("com.google.dagger:dagger-android-support:${Versions.daggerVersion}")

    implementation("io.reactivex.rxjava2:rxandroid:2.0.2")
    implementation("io.reactivex.rxjava2:rxjava:2.2.9")

    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}")
    implementation("com.squareup.okhttp3:okhttp:4.2.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.0")
}

fun BaseFlavor.buildConfigFieldFromGradleProperty(gradlePropertyName: String) {
    val propertyValue = project.properties[gradlePropertyName] as? String
    checkNotNull(propertyValue) { "Gradle property $gradlePropertyName is null" }

    val androidResourceName = "GRADLE_${gradlePropertyName.toSnakeCase()}".toUpperCase()
    buildConfigField("String", androidResourceName, propertyValue)
}

fun String.toSnakeCase() = this.split(Regex("(?=[A-Z])")).joinToString("_") { it.toLowerCase() }

fun DefaultConfig.buildConfigField(name: String, value: Set<String>) {
    // Generates String that holds Java String Array code
    val strValue = value.joinToString(prefix = "{", separator = ",", postfix = "}", transform = { "\"$it\"" })
    buildConfigField("String", name, strValue)
}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("io.github.iosephknecht.gradle.configurations.plugins.test.interceptor")
}

android {
    namespace = "io.github.iosephknecht.gradle.configurations.feature7"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    flavorDimensions += listOf("api")

    productFlavors {
        create("internal") {
            dimension = "api"
        }

        create("external") {
            dimension = "api"
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
}

testInterceptor {
    group.set(io.github.iosephknecht.gradle.configurations.plugins.test.interceptor.Group.GROUP_1)

    withVariants {
        internalDebugTask { named<AbstractTestTask>("testInternalDebugUnitTest") }
        internalReleaseTask { named<AbstractTestTask>("testInternalReleaseUnitTest") }
        externalDebugTask { named<AbstractTestTask>("testExternalDebugUnitTest") }
        externalReleaseTask { named<AbstractTestTask>("testExternalReleaseUnitTest") }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
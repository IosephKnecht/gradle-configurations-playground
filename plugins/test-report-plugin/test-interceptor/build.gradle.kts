plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin.jvmToolchain(8)

gradlePlugin {
    plugins {
        register("test-interceptor") {
            id = "io.github.iosephknecht.gradle.configurations.plugins.test.interceptor"
            implementationClass = "io.github.iosephknecht.gradle.configurations.plugins.test.interceptor.TestInterceptorPlugin"
        }
    }
}

dependencies {
    implementation(project(":test-reports-attributes"))
}
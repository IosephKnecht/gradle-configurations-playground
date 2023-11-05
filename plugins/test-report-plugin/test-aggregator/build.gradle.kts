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
        register("test-aggregator") {
            id = "io.github.iosephknecht.gradle.configurations.plugins.test.aggregator"
            implementationClass =
                "io.github.iosephknecht.gradle.configurations.plugins.test.aggregator.TestAggregatorPlugin"
        }
    }
}

dependencies {
    implementation(project(":test-reports-attributes"))
}
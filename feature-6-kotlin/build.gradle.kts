plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("io.github.iosephknecht.gradle.configurations.plugins.test.interceptor")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin.jvmToolchain(8)

testInterceptor {
    group.set(io.github.iosephknecht.gradle.configurations.plugins.test.interceptor.Group.GROUP_2)
    withoutVariants {
        testTask { this.test }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
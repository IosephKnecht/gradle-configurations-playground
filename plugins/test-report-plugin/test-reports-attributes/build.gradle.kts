plugins {
    `embedded-kotlin`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin.jvmToolchain(8)

dependencies {
    implementation(gradleApi())
}
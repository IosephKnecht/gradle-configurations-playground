package io.github.iosephknecht.gradle.configurations.test.reports.attributes

import org.gradle.api.Named
import org.gradle.api.attributes.Attribute

enum class TestArtifactType(private val namedValue: String) : Named {
    BINARY_RESULTS_DIRECTORY("binary-results-directory");

    override fun getName() = namedValue

    companion object {

        val ATTRIBUTE: Attribute<TestArtifactType> = Attribute.of(TestArtifactType::class.java)
    }
}
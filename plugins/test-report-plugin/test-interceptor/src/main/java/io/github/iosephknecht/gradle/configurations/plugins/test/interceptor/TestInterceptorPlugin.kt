package io.github.iosephknecht.gradle.configurations.plugins.test.interceptor

import io.github.iosephknecht.gradle.configurations.test.reports.attributes.GroupTypeAttribute
import io.github.iosephknecht.gradle.configurations.test.reports.attributes.TestArtifactType
import io.github.iosephknecht.gradle.configurations.test.reports.attributes.VariantTypeAttribute
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class TestInterceptorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension = target.extensions.create(
            TestInterceptorPluginExtension::class.java,
            TestInterceptorPluginExtension.NAME,
            DefaultTestInterceptorPluginExtension::class.java
        ) as DefaultTestInterceptorPluginExtension

        target.afterEvaluate {
            val selectedGroup = extension.group.get()
            val variants = extension.property.get().get()

            target.configurations.create("testReportsConfiguration") {
                isCanBeConsumed = true
                isCanBeResolved = false

                attributes.attribute(
                    TestArtifactType.ATTRIBUTE,
                    TestArtifactType.BINARY_RESULTS_DIRECTORY
                )

                variants.forEach { (key, value) ->
                    outgoing.variants.create(key.getName()) {
                        val provider = target.provider { value.invoke(project.tasks) }
                            .flatMap { it }
                            .map { it.binaryResultsDirectory }
                            .map { it.asFile }

                        attributes {
                            attribute(
                                GroupTypeAttribute.ATTRIBUTE,
                                when (selectedGroup) {
                                    Group.GROUP_1 -> GroupTypeAttribute.GROUP_1
                                    Group.GROUP_2 -> GroupTypeAttribute.GROUP_2
                                }
                            )
                            attribute(
                                VariantTypeAttribute.ATTRIBUTE,
                                key
                            )

                            artifact(provider)
                        }
                    }
                }
            }
        }
    }
}
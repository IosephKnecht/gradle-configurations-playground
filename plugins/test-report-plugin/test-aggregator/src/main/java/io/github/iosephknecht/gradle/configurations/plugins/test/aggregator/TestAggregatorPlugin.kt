package io.github.iosephknecht.gradle.configurations.plugins.test.aggregator

import io.github.iosephknecht.gradle.configurations.test.reports.attributes.GroupTypeAttribute
import io.github.iosephknecht.gradle.configurations.test.reports.attributes.TestArtifactType
import io.github.iosephknecht.gradle.configurations.test.reports.attributes.VariantTypeAttribute
import io.github.iosephknecht.gradle.configurations.test.reports.attributes.traverseAttributes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.AttributeCompatibilityRule
import org.gradle.api.attributes.CompatibilityCheckDetails
import org.gradle.api.tasks.testing.TestReport
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.register
import org.gradle.language.base.plugins.LifecycleBasePlugin
import java.util.concurrent.Callable

internal class TestAggregatorPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        require(target.project.parent == null)

        target.evaluationDependsOnChildren()

        val aggregateReports = target.configurations.create(INVISIBLE_CONFIGURATION_NAME) {
            isCanBeResolved = false
            isCanBeConsumed = false
            isVisible = false
        }

        val resolvableReportsConfiguration =
            target.configurations.create(RESOLVABLE_CONFIGURATION_NAME) {
                isCanBeResolved = true
                isCanBeConsumed = false

                attributes {
                    attribute(
                        TestArtifactType.ATTRIBUTE,
                        TestArtifactType.BINARY_RESULTS_DIRECTORY
                    )
                }

                extendsFrom(aggregateReports)
            }

        target
            .dependencies
            .attributesSchema
            .attribute(TestArtifactType.ATTRIBUTE) {
                compatibilityRules.add(TestArtifactTypeCompatibilityRule::class.java)
            }

        traverseAttributes { groupType, variantType ->
            target.tasks.register<TestReport>(
                "test${
                    variantType.getName().capitalized()
                }${groupType.getName().capitalized()}UnitTest"
            ) {
                group = LifecycleBasePlugin.VERIFICATION_GROUP

                val callable = Callable {
                    resolvableReportsConfiguration
                        .incoming
                        .artifactView {
                            attributes {
                                attribute(
                                    VariantTypeAttribute.ATTRIBUTE,
                                    variantType
                                )
                                attribute(
                                    GroupTypeAttribute.ATTRIBUTE,
                                    groupType
                                )
                            }
                        }
                        .files
                }

                testResults.from(callable)
                destinationDirectory.set(target.layout.buildDirectory.dir("test-reports"))
            }
        }

        target.afterEvaluate {
            subprojects subproject@{
                if (project.plugins.hasPlugin(INTERCEPTOR_PLUGIN_ID)) {
                    target.dependencies.add(aggregateReports.name, this@subproject)
                }
            }
        }
    }

    class TestArtifactTypeCompatibilityRule : AttributeCompatibilityRule<TestArtifactType> {

        override fun execute(details: CompatibilityCheckDetails<TestArtifactType>) {
            val producedValue = details.producerValue
            val consumedValue = details.consumerValue

            if (producedValue != null && consumedValue != null && producedValue == consumedValue) {
                details.compatible()
                return
            }

            error("unsupported dependency for configuration $INVISIBLE_CONFIGURATION_NAME")
        }
    }

    private companion object {

        const val INVISIBLE_CONFIGURATION_NAME = "aggregateReports"
        const val RESOLVABLE_CONFIGURATION_NAME = "resolvableReportsConfiguration"

        const val INTERCEPTOR_PLUGIN_ID =
            "io.github.iosephknecht.gradle.configurations.plugins.test.interceptor"
    }
}
package io.github.iosephknecht.gradle.configurations.plugins.test.interceptor

import io.github.iosephknecht.gradle.configurations.test.reports.attributes.VariantTypeAttribute
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

sealed interface TestTasksContainer {

    interface WithoutVariant : TestTasksContainer {

        fun <T : AbstractTestTask> testTask(block: TaskContainer.() -> TaskProvider<T>)
    }

    interface WithVariants : TestTasksContainer {

        fun internalReleaseTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>)

        fun externalReleaseTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>)

        fun internalDebugTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>)

        fun externalDebugTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>)
    }
}

internal interface InternalTestTaskContainer {

    fun get(): Map<VariantTypeAttribute, TaskContainer.() -> TaskProvider<AbstractTestTask>>
}

interface TestInterceptorPluginExtension {

    val group: Property<Group>

    fun withVariants(action: Action<TestTasksContainer.WithVariants>)

    fun withoutVariants(action: Action<TestTasksContainer.WithoutVariant>)

    companion object {

        const val NAME = "testInterceptor"
    }
}

internal abstract class DefaultTestInterceptorPluginExtension @Inject constructor(
    private val objectsFactory: ObjectFactory
) : TestInterceptorPluginExtension {

    val property = objectsFactory
        .property<InternalTestTaskContainer>()

    override val group: Property<Group> = objectsFactory.property<Group>()
        .convention(Group.GROUP_1)

    override fun withVariants(
        action: Action<TestTasksContainer.WithVariants>
    ) = DefaultWithVariants(objectsFactory.mapProperty())
        .apply(action::execute)
        .let(property::set)

    override fun withoutVariants(
        action: Action<TestTasksContainer.WithoutVariant>
    ) = DefaultWithoutVariants(objectsFactory.mapProperty())
        .apply(action::execute)
        .let(property::set)

    private class DefaultWithVariants(
        private val mapProperty: MapProperty<VariantTypeAttribute, TaskContainer.() -> TaskProvider<AbstractTestTask>>
    ) : TestTasksContainer.WithVariants, InternalTestTaskContainer {

        override fun internalReleaseTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>) {
            mapProperty.put(VariantTypeAttribute.INTERNAL_RELEASE, block)
        }

        override fun externalReleaseTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>) {
            mapProperty.put(VariantTypeAttribute.EXTERNAL_RELEASE, block)
        }

        override fun internalDebugTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>) {
            mapProperty.put(VariantTypeAttribute.INTERNAL_DEBUG, block)
        }

        override fun externalDebugTask(block: TaskContainer.() -> TaskProvider<AbstractTestTask>) {
            mapProperty.put(VariantTypeAttribute.EXTERNAL_DEBUG, block)
        }

        override fun get(): Map<VariantTypeAttribute, TaskContainer.() -> TaskProvider<AbstractTestTask>> {
            return mapProperty.get()
        }

    }

    private class DefaultWithoutVariants(
        private val mapProperty: MapProperty<VariantTypeAttribute, TaskContainer.() -> TaskProvider<AbstractTestTask>>
    ) : TestTasksContainer.WithoutVariant, InternalTestTaskContainer {

        override fun <T : AbstractTestTask> testTask(block: TaskContainer.() -> TaskProvider<T>) {
            VariantTypeAttribute
                .values()
                .forEach {
                    mapProperty.put(
                        it,
                        block as TaskContainer.() -> TaskProvider<AbstractTestTask>
                    )
                }
        }

        override fun get(): Map<VariantTypeAttribute, TaskContainer.() -> TaskProvider<AbstractTestTask>> {
            return mapProperty.get()
        }
    }
}
package io.github.iosephknecht.gradle.configurations.test.reports.attributes

import org.gradle.api.Named
import org.gradle.api.attributes.Attribute

enum class VariantTypeAttribute(private val namedValue: String) : Named {
    INTERNAL_DEBUG("internalDebug"),
    INTERNAL_RELEASE("internalRelease"),
    EXTERNAL_DEBUG("externalDebug"),
    EXTERNAL_RELEASE("externalRelease");

    override fun getName(): String = namedValue

    companion object {

        val ATTRIBUTE: Attribute<VariantTypeAttribute> =
            Attribute.of(VariantTypeAttribute::class.java)
    }
}
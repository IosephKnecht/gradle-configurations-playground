package io.github.iosephknecht.gradle.configurations.test.reports.attributes

inline fun traverseAttributes(block: (groupAttribute: GroupTypeAttribute, variantAttribute: VariantTypeAttribute) -> Unit) {
    GroupTypeAttribute.values().forEach { groupAttribute ->
        VariantTypeAttribute.values().forEach { variantAttribute ->
            block.invoke(groupAttribute, variantAttribute)
        }
    }
}
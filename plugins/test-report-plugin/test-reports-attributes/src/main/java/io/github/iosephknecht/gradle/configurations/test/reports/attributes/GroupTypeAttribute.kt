package io.github.iosephknecht.gradle.configurations.test.reports.attributes

import org.gradle.api.Named
import org.gradle.api.attributes.Attribute

enum class GroupTypeAttribute(private val namedValue: String) : Named {
    GROUP_1("group1"),
    GROUP_2("group2");

    override fun getName(): String = namedValue

    companion object {

        val ATTRIBUTE: Attribute<GroupTypeAttribute> = Attribute.of(GroupTypeAttribute::class.java)
    }
}
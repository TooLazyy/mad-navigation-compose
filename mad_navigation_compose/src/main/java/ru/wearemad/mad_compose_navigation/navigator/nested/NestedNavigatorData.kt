package ru.wearemad.mad_compose_navigation.navigator.nested

/**
 * Nested navigator's data including
 * @param screenKey - screen tht navigator belongs
 * @param navigator - navigator instance
 */
data class NestedNavigatorData(
    val screenKey: String,
    val navigator: NestedNavigator
)
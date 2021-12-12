package ru.wearemad.mad_compose_navigation.navigator.nested

import ru.wearemad.mad_compose_navigation.navigator.base.NavigatorState

/**
 * State of nested navigator
 */
data class NestedNavigatorState(
    val screenKey: String,
    val state: NavigatorState,
)
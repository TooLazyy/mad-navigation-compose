package ru.wearemad.mad_compose_navigation.navigator.base

import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorState
import ru.wearemad.mad_compose_navigation.route.Route

/**
 * Navigator's state including all nested navigators states
 */
data class NavigatorState(
    val currentRoute: Route? = null,
    val currentStack: List<Route> = listOf(),
    val currentDialogsStack: List<Route> = listOf(),
    val nestedNavigatorsState: List<NestedNavigatorState> = listOf(),
    val stateChangedAtLeastOnce: Boolean = false
)
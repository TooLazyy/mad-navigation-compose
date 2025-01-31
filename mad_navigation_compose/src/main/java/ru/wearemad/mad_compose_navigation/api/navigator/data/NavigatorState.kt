package ru.wearemad.mad_compose_navigation.api.navigator.data

import ru.wearemad.mad_compose_navigation.api.route.Route

data class NavigatorState(
    val screenId: String,
    val currentStack: List<Route> = listOf(),
    val currentDialogsStack: List<Route> = listOf(),
    val nestedNavigatorsState: List<NavigatorState> = listOf(),
    val withAnimation: Boolean = false
) {

    val currentRoute: Route?
        get() = currentStack.lastOrNull()
}
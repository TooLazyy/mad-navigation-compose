package ru.wearemad.mad_compose_navigation.api.navigator.data

import ru.wearemad.mad_compose_navigation.api.route.Route

data class NavigatorState(
    val instanceId: String,
    val currentStack: List<StackEntry> = listOf(),
    val currentDialogsStack: List<StackEntry> = listOf(),
    val nestedNavigatorsState: List<NavigatorState> = listOf(),
    val withAnimation: Boolean = false
) {

    val currentEntry: StackEntry?
        get() = currentStack.lastOrNull()

    val currentRoute: Route?
        get() = currentEntry?.route
}
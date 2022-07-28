package ru.wearemad.mad_compose_navigation.api.restorer

import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.route.Route

class RestoreStateResult(
    val currentStack: List<Route> = listOf(),
    val currentDialogsStack: List<Route> = listOf(),
    val nestedNavigators: List<NestedNavigatorData> = listOf(),
)
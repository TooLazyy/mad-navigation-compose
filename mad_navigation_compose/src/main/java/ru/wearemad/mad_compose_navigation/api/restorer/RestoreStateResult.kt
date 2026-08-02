package ru.wearemad.mad_compose_navigation.api.restorer

import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData

class RestoreStateResult(
    val currentStack: List<StackEntry> = listOf(),
    val currentDialogsStack: List<StackEntry> = listOf(),
    val nestedNavigators: List<NestedNavigatorData> = listOf(),
)
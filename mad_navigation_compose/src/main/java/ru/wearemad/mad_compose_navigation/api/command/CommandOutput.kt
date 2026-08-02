package ru.wearemad.mad_compose_navigation.api.command

import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry

class CommandOutput(
    val newScreensStack: List<StackEntry>,
    val newDialogsStack: List<StackEntry>,
    val withAnimation: Boolean,
)
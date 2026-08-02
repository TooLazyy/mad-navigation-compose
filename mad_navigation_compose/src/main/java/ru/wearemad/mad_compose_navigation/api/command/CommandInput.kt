package ru.wearemad.mad_compose_navigation.api.command

import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry

class CommandInput(
    val screensStack: List<StackEntry>,
    val dialogsStack: List<StackEntry>
)
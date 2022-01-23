package ru.wearemad.mad_compose_navigation.command

import ru.wearemad.mad_compose_navigation.route.Route

class CommandOutput(
    val newScreensStack: List<Route>,
    val newDialogsStack: List<Route>,
)
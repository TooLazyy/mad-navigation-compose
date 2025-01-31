package ru.wearemad.mad_compose_navigation.api.command

import ru.wearemad.mad_compose_navigation.api.route.Route

class CommandOutput(
    val newScreensStack: List<Route>,
    val newDialogsStack: List<Route>,
    val withAnimation: Boolean,
)
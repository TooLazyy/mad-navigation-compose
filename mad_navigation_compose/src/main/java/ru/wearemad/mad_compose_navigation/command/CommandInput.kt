package ru.wearemad.mad_compose_navigation.command

import ru.wearemad.mad_compose_navigation.route.Route

class CommandInput(
    val screensStack: List<Route>,
    val dialogsStack: List<Route>
)
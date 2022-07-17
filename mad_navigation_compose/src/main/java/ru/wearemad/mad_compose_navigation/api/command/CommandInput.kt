package ru.wearemad.mad_compose_navigation.api.command

import ru.wearemad.mad_compose_navigation.api.route.Route

class CommandInput(
    val screensStack: List<Route>,
    val dialogsStack: List<Route>
)
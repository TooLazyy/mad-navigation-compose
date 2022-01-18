package ru.wearemad.mad_compose_navigation.router

import ru.wearemad.mad_compose_navigation.command.Add
import ru.wearemad.mad_compose_navigation.command.Back
import ru.wearemad.mad_compose_navigation.command.NewRoot
import ru.wearemad.mad_compose_navigation.command.OpenAsDialog
import ru.wearemad.mad_compose_navigation.command.Replace
import ru.wearemad.mad_compose_navigation.route.Route

suspend fun Router.back(includeDialogs: Boolean = true) {
    executeCommands(Back(includeDialogs))
}

suspend fun Router.add(route: Route) {
    executeCommands(Add(route))
}

suspend fun Router.newRoot(
    route: Route,
    includeDialogs: Boolean = true
) {
    executeCommands(
        NewRoot(includeDialogs, route)
    )
}

suspend fun Router.replace(route: Route) {
    executeCommands(Replace(route))
}

suspend fun Router.openDialog(route: Route) {
    executeCommands(OpenAsDialog(route))
}
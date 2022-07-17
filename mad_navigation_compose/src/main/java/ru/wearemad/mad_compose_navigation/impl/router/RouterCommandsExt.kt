package ru.wearemad.mad_compose_navigation.impl.router

import ru.wearemad.mad_compose_navigation.impl.command.Add
import ru.wearemad.mad_compose_navigation.impl.command.Back
import ru.wearemad.mad_compose_navigation.impl.command.NewRoot
import ru.wearemad.mad_compose_navigation.impl.command.OpenAsDialog
import ru.wearemad.mad_compose_navigation.impl.command.Replace
import ru.wearemad.mad_compose_navigation.api.route.Route

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
        NewRoot(route, includeDialogs)
    )
}

suspend fun Router.replace(route: Route) {
    executeCommands(Replace(route))
}

suspend fun Router.openDialog(route: Route) {
    executeCommands(OpenAsDialog(route))
}
package ru.wearemad.mad_compose_navigation.impl.router

import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.impl.command.Add
import ru.wearemad.mad_compose_navigation.impl.command.Back
import ru.wearemad.mad_compose_navigation.impl.command.NewRoot
import ru.wearemad.mad_compose_navigation.impl.command.OpenAsDialog
import ru.wearemad.mad_compose_navigation.impl.command.Replace

suspend fun Router.back(
    withAnimation: Boolean,
    includeDialogs: Boolean = true
) {
    executeCommands(
        Back(
            withAnimation = withAnimation,
            includeDialogs = includeDialogs,
        )
    )
}

suspend fun Router.add(
    route: Route,
    withAnimation: Boolean,
) {
    executeCommands(
        Add(
            route,
            withAnimation = withAnimation,
        )
    )
}

suspend fun Router.newRoot(
    route: Route,
    withAnimation: Boolean,
    includeDialogs: Boolean = true
) {
    executeCommands(
        NewRoot(
            route,
            withAnimation = withAnimation,
            includeDialogs = includeDialogs,
        )
    )
}

suspend fun Router.replace(
    route: Route,
    withAnimation: Boolean,
) {
    executeCommands(
        Replace(
            route,
            withAnimation = withAnimation,
        )
    )
}

suspend fun Router.openDialog(route: Route) {
    executeCommands(OpenAsDialog(route))
}
package ru.wearemad.mad_compose_navigation.impl.router

import ru.wearemad.mad_compose_navigation.api.command.Command
import ru.wearemad.mad_compose_navigation.api.command.CommandsExecutor
import ru.wearemad.mad_compose_navigation.api.router.RouterNavigatorHolder

/**
 * Default app router.
 * Holds RouterNavigatorHolder and delegates executeCommands calls to it
 */
open class Router : CommandsExecutor {

    internal val holder: RouterNavigatorHolder = DefaultRouterNavigatorHolder()

    override suspend fun executeCommands(vararg commands: Command) {
        holder.executeCommands(*commands)
    }
}
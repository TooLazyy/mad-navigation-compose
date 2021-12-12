package ru.wearemad.mad_compose_navigation.router

import ru.wearemad.mad_compose_navigation.command.Command
import ru.wearemad.mad_compose_navigation.navigator.base.CommandsExecutor
import ru.wearemad.mad_compose_navigation.router.holder.DefaultRouterNavigatorHolder
import ru.wearemad.mad_compose_navigation.router.holder.RouterNavigatorHolder

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
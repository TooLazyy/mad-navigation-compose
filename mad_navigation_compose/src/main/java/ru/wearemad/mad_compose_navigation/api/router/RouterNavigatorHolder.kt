package ru.wearemad.mad_compose_navigation.api.router

import ru.wearemad.mad_compose_navigation.api.command.CommandsExecutor

/**
 * Holder for CommandsExecutor(a navigator's instance) which allows to attach
 * and detach from that executor in order to respect app lifecycle
 */
interface RouterNavigatorHolder : CommandsExecutor {

    /**
     * Attaches CommandsExecutor to a holder, then send all pending commands to it
     */
    suspend fun attachNavigator(navigator: CommandsExecutor)

    /**
     * Detaches from navigator. After this method is called, CommandsExecutor will receive no more events
     */
    fun detachNavigator()
}
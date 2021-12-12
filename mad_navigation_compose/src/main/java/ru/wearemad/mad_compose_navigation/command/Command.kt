package ru.wearemad.mad_compose_navigation.command

import ru.wearemad.mad_compose_navigation.route.Route

/**
 * Base interface for Navigator's commands
 */
interface Command {

    /**
     * Accepts current stack and returns a new one as result
     */
    fun execute(currentStack: List<Route>): List<Route>
}
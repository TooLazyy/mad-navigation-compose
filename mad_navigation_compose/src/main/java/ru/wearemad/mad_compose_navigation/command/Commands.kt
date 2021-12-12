package ru.wearemad.mad_compose_navigation.command

import ru.wearemad.mad_compose_navigation.route.Route

/**
 * Clears current stack and sets a new one as single route
 * @param route - router to set
 */
class NewRoot(
    private val route: Route
) : Command {

    override fun execute(currentStack: List<Route>): List<Route> = listOf(route)
}

/**
 * Adds a new screen at the end of the screens stack
 * @param route - route to add to the end
 */
class Add(
    private val route: Route
) : Command {

    override fun execute(currentStack: List<Route>): List<Route> =
        currentStack.toMutableList().apply {
            add(route)
        }
}

/**
 * Remove a most recent rout from stack
 */
class Back : Command {

    override fun execute(currentStack: List<Route>): List<Route> =
        currentStack.toMutableList().apply {
            if (isNotEmpty()) {
                removeAt(currentStack.lastIndex)
            }
        }
}

/**
 * Navigates back to a particular route
 * @param route to get back to
 */
class BackTo(
    private val route: Route
) : Command {

    override fun execute(currentStack: List<Route>): List<Route> =
        currentStack.toMutableList().apply {
            val index = currentStack.indexOfFirst { it.screenKey == route.screenKey }
            if (index != -1) {
                while (currentStack.size != index + 1) {
                    removeAt(currentStack.lastIndex)
                }
            }
        }
}

/**
 * Navigates back to a very first route
 */
class BackToRoot : Command {

    override fun execute(currentStack: List<Route>): List<Route> =
        if (currentStack.isEmpty()) {
            currentStack
        } else {
            listOf(currentStack.first())
        }
}

/**
 * Replace last route with a new one
 * @param route to replace the last one
 */
class Replace(
    private val route: Route
) : Command {

    override fun execute(currentStack: List<Route>): List<Route> =
        currentStack.toMutableList().apply {
            if (isNotEmpty()) {
                removeAt(currentStack.lastIndex)
            }
            add(route)
        }
}
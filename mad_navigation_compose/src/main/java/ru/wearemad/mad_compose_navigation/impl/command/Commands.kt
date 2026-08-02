package ru.wearemad.mad_compose_navigation.impl.command

import ru.wearemad.mad_compose_navigation.api.command.Command
import ru.wearemad.mad_compose_navigation.api.command.CommandInput
import ru.wearemad.mad_compose_navigation.api.command.CommandOutput
import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry
import ru.wearemad.mad_compose_navigation.api.route.Route

/**
 * Clears current stack and sets a new one as single route
 * @param route - router to set
 */
class NewRoot(
    private val route: Route,
    private val withAnimation: Boolean,
    private val includeDialogs: Boolean = true,
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = CommandOutput(
        listOf(StackEntry.of(route)),
        if (includeDialogs) {
            listOf()
        } else {
            input.dialogsStack
        },
        withAnimation = withAnimation,
    )
}

/**
 * Adds a new screen at the end of the screens stack
 * @param route - route to add to the end
 */
class Add(
    private val route: Route,
    private val withAnimation: Boolean,
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = CommandOutput(
        input.screensStack.toMutableList().apply {
            add(StackEntry.of(route))
        },
        input.dialogsStack,
        withAnimation = withAnimation,
    )
}

/**
 * Remove a most recent rout from stack
 */
class Back(
    private val withAnimation: Boolean,
    private val includeDialogs: Boolean = true,
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = applyCommand(
        input.screensStack,
        input.dialogsStack
    )

    private fun applyCommand(
        screensStack: List<StackEntry>,
        dialogsStack: List<StackEntry>
    ): CommandOutput {
        if (includeDialogs.not()) {
            return backWithoutDialogRoutes(
                screensStack,
                dialogsStack
            )
        }
        if (dialogsStack.isEmpty()) {
            return backWithoutDialogRoutes(
                screensStack,
                dialogsStack
            )
        }
        return CommandOutput(
            screensStack,
            dialogsStack.toMutableList().apply {
                removeAt(dialogsStack.lastIndex)
            },
            withAnimation = withAnimation,
        )
    }

    private fun backWithoutDialogRoutes(
        screensStack: List<StackEntry>,
        dialogsStack: List<StackEntry>
    ): CommandOutput = CommandOutput(
        screensStack.toMutableList().apply {
            if (isNotEmpty()) {
                removeAt(screensStack.lastIndex)
            }
        },
        dialogsStack,
        withAnimation = withAnimation,
    )
}

/**
 * Navigates back to a particular route
 * @param route to get back to
 */
class BackTo(
    private val route: Route,
    private val withAnimation: Boolean,
    private val closeDialogs: Boolean = true
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = CommandOutput(
        input.screensStack.toMutableList().apply {
            val index = input.screensStack.indexOfFirst { it.screenKey == route.screenKey }
            if (index != -1) {
                while (size != index + 1) {
                    removeAt(lastIndex)
                }
            }
        },
        if (closeDialogs) {
            listOf()
        } else {
            input.dialogsStack
        },
        withAnimation = withAnimation,
    )
}

/**
 * Navigates back to a very first route
 */
class BackToRoot(
    private val withAnimation: Boolean,
    private val closeDialogs: Boolean = true,
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = CommandOutput(
        if (input.screensStack.isEmpty()) {
            input.screensStack
        } else {
            listOf(input.screensStack.first())
        },
        if (closeDialogs) {
            input.dialogsStack
        } else {
            listOf()
        },
        withAnimation = withAnimation
    )
}

/**
 * Replace last route with a new one
 * @param route to replace the last one
 */
class Replace(
    private val route: Route,
    private val withAnimation: Boolean,
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = CommandOutput(
        input.screensStack.toMutableList().apply {
            if (isNotEmpty()) {
                removeAt(input.screensStack.lastIndex)
            }
            add(StackEntry.of(route))
        },
        input.dialogsStack,
        withAnimation = withAnimation,
    )
}

/**
 * Open a screen as dialog, i.e. in a different window
 * @param route to replace the last one
 */
class OpenAsDialog(
    private val route: Route
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput = CommandOutput(
        input.screensStack,
        input.dialogsStack.toMutableList().apply {
            add(StackEntry.of(route))
        },
        withAnimation = false,
    )
}
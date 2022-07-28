package ru.wearemad.mad_compose_navigation.api.command

/**
 * Base interface for Navigator's commands
 */
interface Command {

    /**
     * Accepts current stack and returns a new one as result
     */
    fun execute(
        input: CommandInput
    ): CommandOutput
}
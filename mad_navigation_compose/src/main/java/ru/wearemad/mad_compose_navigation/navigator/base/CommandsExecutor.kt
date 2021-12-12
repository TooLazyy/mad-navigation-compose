package ru.wearemad.mad_compose_navigation.navigator.base

import androidx.annotation.MainThread
import ru.wearemad.mad_compose_navigation.command.Command

/**
 * Main interface for commands executor
 * Should implement logic of how commands are applied
 */
interface CommandsExecutor {

    @MainThread
    suspend fun executeCommands(vararg commands: Command)
}
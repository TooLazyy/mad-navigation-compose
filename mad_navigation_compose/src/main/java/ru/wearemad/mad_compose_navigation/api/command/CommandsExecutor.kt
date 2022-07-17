package ru.wearemad.mad_compose_navigation.api.command

import androidx.annotation.MainThread

/**
 * Main interface for commands executor
 * Should implement logic of how commands are applied
 */
interface CommandsExecutor {

    @MainThread
    suspend fun executeCommands(vararg commands: Command)
}
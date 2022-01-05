package ru.wearemad.mad_compose_navigation.navigator.base

import ru.wearemad.mad_compose_navigation.command.Command

/**
 * Navigator's inner events
 */
sealed class NavigatorEvent {

    class ExecuteCommands(
        val commands: Array<out Command>
    ) : NavigatorEvent()

    object OnNestedNavigatorsStackChanged : NavigatorEvent()
}
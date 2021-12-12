package ru.wearemad.mad_compose_navigation.navigator.base

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.command.Command
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator

/**
 * Navigator's inner events
 */
sealed class NavigatorEvent {

    class ExecuteCommands(
        val commands: Array<out Command>
    ) : NavigatorEvent()

    class RestoreState(
        val inState: Bundle,
        val nestedNavigatorFactory: () -> NestedNavigator
    ) : NavigatorEvent()

    object OnNestedNavigatorsStackChanged : NavigatorEvent()
}
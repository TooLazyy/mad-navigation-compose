package ru.wearemad.mad_compose_navigation.navigator.base

import kotlinx.coroutines.flow.StateFlow
import ru.wearemad.mad_compose_navigation.back_press.handler.BackPressHandler
import ru.wearemad.mad_compose_navigation.back_press.handler.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorsHost

/**
 * Main navigator's interface
 * Navigator is responsible for dealing with nested navigators(get, create, cleanup),
 * handles back press events, execute navigation commands (self or send it to registered listeners) and
 * save/restore state
 */
interface Navigator :
    NestedNavigatorsHost,
    BackPressHandler,
    ChildrenBackPressHandler,
    CommandsExecutor,
    CustomCoroutineScope {

    val navigatorStateFlow: StateFlow<NavigatorState>
}

interface CustomCoroutineScope {

    fun cancelJobs()
}
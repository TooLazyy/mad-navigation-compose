package ru.wearemad.mad_compose_navigation.navigator.base

import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorsHost
import kotlinx.coroutines.flow.StateFlow
import ru.wearemad.mad_compose_navigation.back_press.handler.BackPressHandler
import ru.wearemad.mad_compose_navigation.back_press.handler.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.navigator.saveable.SaveableNavigator

/**
 * Main navigator's interface
 * Navigator is responsible for dealing with nested navigators(get, create, cleanup),
 * handles back press events, execute navigation commands (self or send it to registered listeners) and
 * save/restore state
 */
interface Navigator :
    SaveableNavigator,
    NestedNavigatorsHost,
    BackPressHandler,
    ChildrenBackPressHandler,
    CommandsExecutor,
    CustomCoroutineScope {

    companion object {

        const val KEY_COMPOSE_NAVIGATORS_DATA = "key_compose_navigators_data"
        const val KEY_COMPOSE_NESTED_NAVIGATORS_DATA = "key_compose_nested_navigators_data"
    }

    val navigatorStateFlow: StateFlow<NavigatorState>
}

interface CustomCoroutineScope {

    fun cancelJobs()
}
package ru.wearemad.mad_compose_navigation.navigator.nested

import ru.wearemad.mad_compose_navigation.navigator.base.NavigatorState

/**
 * Listener to be triggered when nested navigator's state has been changed
 */
fun interface NestedNavigatorStateChangedListener {

    fun onChanged(state: NavigatorState)
}
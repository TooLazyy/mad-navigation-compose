package ru.wearemad.mad_compose_navigation.navigator.nested

import ru.wearemad.mad_compose_navigation.navigator.base.Navigator

/**
 * Nested navigator, sends it's stack changes up to parent one
 */
interface NestedNavigator : Navigator {

    fun addNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener)

    fun removeNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener)
}
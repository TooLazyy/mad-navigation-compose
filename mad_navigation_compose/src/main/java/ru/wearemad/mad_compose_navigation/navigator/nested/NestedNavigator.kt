package ru.wearemad.mad_compose_navigation.navigator.nested

import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.saveable.RestorableNestedNavigator
import ru.wearemad.mad_compose_navigation.navigator.saveable.SaveableNestedNavigator

/**
 * Nested navigator, sends it's stack changes up to parent one
 */
interface NestedNavigator :
    Navigator,
    RestorableNestedNavigator,
    SaveableNestedNavigator {

    fun addNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener)

    fun removeNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener)
}
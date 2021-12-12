package ru.wearemad.mad_compose_navigation.navigator

import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorStateChangedListener

class AppNestedNavigator(
    canGoBack: Boolean = false
) : AppNavigator(canGoBack),
    NestedNavigator {

    private var stateChangedListeners: MutableSet<NestedNavigatorStateChangedListener> = mutableSetOf()

    override fun addNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener) {
        stateChangedListeners.add(listener)
    }

    override fun removeNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener) {
        stateChangedListeners.remove(listener)
    }

    override fun afterStackChanged() {
        stateChangedListeners.forEach {
            it.onChanged(navigatorState)
        }
    }
}
package ru.wearemad.mad_compose_navigation.navigator.saveable

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator

/**
 * Allows to save navigator during config changes
 */
interface SaveableNavigator {

    fun saveState(): Bundle

    fun restoreState(inState: Bundle, nestedNavigatorFactory: () -> NestedNavigator)
}
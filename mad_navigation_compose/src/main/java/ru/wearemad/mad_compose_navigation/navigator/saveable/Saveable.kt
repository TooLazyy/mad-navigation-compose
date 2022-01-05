package ru.wearemad.mad_compose_navigation.navigator.saveable

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator

/**
 * Allows to save navigator during config changes
 */
interface SaveableNavigator {

    fun saveState(): Bundle
}

/**
 * Allows to restore navigator
 */
interface RestorableNavigator {

    fun restoreState(inState: Bundle, nestedNavigatorFactory: () -> NestedNavigator)
}

/**
 * Allows to save nested navigator
 */
interface SaveableNestedNavigator {

    fun saveState(screenId: String): Bundle
}

/**
 * Allows to restore nested navigator
 */
interface RestorableNestedNavigator {

    suspend fun restoreState(
        inState: Bundle,
        screenId: String,
        nestedNavigatorFactory: () -> NestedNavigator
    )
}
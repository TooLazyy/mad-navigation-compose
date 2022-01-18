package ru.wearemad.mad_compose_navigation.navigator.nested

import android.os.Bundle
import kotlinx.coroutines.flow.Flow
import ru.wearemad.mad_compose_navigation.navigator.base.NavigatorState
import ru.wearemad.mad_compose_navigation.route.Route

typealias NestedNavigatorStackChangedEvent = Pair<String, NavigatorState>

/**
 * Describes how to interact with nested navigators
 */
interface NestedNavigatorsHost {

    val nestedNavigators: List<NestedNavigatorData>

    val onNestedNavigatorStackChangedFlow: Flow<NestedNavigatorStackChangedEvent>

    fun getOrCreateNestedNavigator(
        screenKey: String,
        factory: () -> NestedNavigator
    ): NestedNavigator

    fun findNestedNavigator(screenKey: String): NestedNavigator?

    fun removeNestedNavigator(screenKey: String)

    suspend fun restoreNestedNavigators(
        inState: Bundle?,
        navigatorFactory: () -> NestedNavigator,
        nestedNavigatorFactory: () -> NestedNavigator,
    )

    fun prepareNestedNavigatorSavedState(): Bundle

    fun clearUnusedNestedNavigators(routesList: List<Route>)
}
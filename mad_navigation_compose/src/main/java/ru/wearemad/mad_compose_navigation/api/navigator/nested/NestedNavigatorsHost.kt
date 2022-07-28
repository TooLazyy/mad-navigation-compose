package ru.wearemad.mad_compose_navigation.api.navigator.nested

import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.route.Route

interface NestedNavigatorsHost {

    val nestedNavigators: List<NestedNavigatorData>

    fun setNavigators(list: List<NestedNavigatorData>)

    fun getOrCreateNestedNavigator(
        screenKey: String,
        factory: NestedNavigatorFactory
    ): Navigator

    fun findNestedNavigator(screenKey: String): Navigator?

    fun removeNestedNavigator(screenKey: String)

    fun clearUnusedNestedNavigators(routesList: List<Route>)
}
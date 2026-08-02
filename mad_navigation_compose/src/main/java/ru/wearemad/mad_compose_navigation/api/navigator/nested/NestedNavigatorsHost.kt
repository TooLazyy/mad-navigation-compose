package ru.wearemad.mad_compose_navigation.api.navigator.nested

import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData

interface NestedNavigatorsHost {

    val nestedNavigators: List<NestedNavigatorData>

    fun setNavigators(list: List<NestedNavigatorData>)

    fun getOrCreateNestedNavigator(
        instanceId: String,
        factory: NestedNavigatorFactory
    ): Navigator

    fun findNestedNavigator(instanceId: String): Navigator?

    fun removeNestedNavigator(instanceId: String)

    fun clearUnusedNestedNavigators(routesList: List<StackEntry>)
}
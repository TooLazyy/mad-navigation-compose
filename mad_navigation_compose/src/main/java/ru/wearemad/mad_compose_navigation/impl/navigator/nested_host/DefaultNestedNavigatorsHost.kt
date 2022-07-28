package ru.wearemad.mad_compose_navigation.impl.navigator.nested_host

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.route.Route

class DefaultNestedNavigatorsHost(
    private val parentOutEventsChannel: Channel<NavigatorEventsChannelEvent>
) : NestedNavigatorsHost {

    override var nestedNavigators: List<NestedNavigatorData> = listOf()

    override fun setNavigators(list: List<NestedNavigatorData>) {
        nestedNavigators = list
    }

    override fun getOrCreateNestedNavigator(
        screenKey: String,
        factory: NestedNavigatorFactory
    ): Navigator {
        val index = nestedNavigators.indexOfFirst { it.screenKey == screenKey }
        if (index >= 0) {
            return nestedNavigators[index].navigator
        }
        val navigator = factory(screenKey, parentOutEventsChannel)
        nestedNavigators = nestedNavigators.toMutableList().apply {
            add(NestedNavigatorData(screenKey, navigator))
        }
        return navigator
    }

    override fun findNestedNavigator(
        screenKey: String
    ): Navigator? = nestedNavigators
        .find { it.screenKey == screenKey }
        ?.navigator

    override fun removeNestedNavigator(screenKey: String) {
        nestedNavigators = nestedNavigators
            .filterNot { it.screenKey == screenKey }
    }

    override fun clearUnusedNestedNavigators(routesList: List<Route>) {
        val routesKeysList = routesList
            .map { it.screenKey }
            .toSet()
        nestedNavigators = nestedNavigators
            .filter { routesKeysList.contains(it.screenKey) }
    }
}
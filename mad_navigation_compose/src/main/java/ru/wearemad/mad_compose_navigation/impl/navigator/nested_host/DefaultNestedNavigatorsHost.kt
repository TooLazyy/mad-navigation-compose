package ru.wearemad.mad_compose_navigation.impl.navigator.nested_host

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent
import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData

class DefaultNestedNavigatorsHost(
    private val parentOutEventsChannel: Channel<NavigatorEventsChannelEvent>
) : NestedNavigatorsHost {

    override var nestedNavigators: List<NestedNavigatorData> = listOf()

    override fun setNavigators(list: List<NestedNavigatorData>) {
        nestedNavigators = list
    }

    override fun getOrCreateNestedNavigator(
        instanceId: String,
        factory: NestedNavigatorFactory
    ): Navigator {
        val index = nestedNavigators.indexOfFirst { it.instanceId == instanceId }
        if (index >= 0) {
            return nestedNavigators[index].navigator
        }
        val navigator = factory(instanceId, parentOutEventsChannel)
        nestedNavigators = nestedNavigators.toMutableList().apply {
            add(NestedNavigatorData(instanceId, navigator))
        }
        return navigator
    }

    override fun findNestedNavigator(
        instanceId: String
    ): Navigator? = nestedNavigators
        .find { it.instanceId == instanceId }
        ?.navigator

    override fun removeNestedNavigator(instanceId: String) {
        nestedNavigators = nestedNavigators
            .filterNot { it.instanceId == instanceId }
    }

    override fun clearUnusedNestedNavigators(routesList: List<StackEntry>) {
        val routesKeysList = routesList
            .map { it.instanceId }
            .toSet()
        nestedNavigators = nestedNavigators
            .filter { routesKeysList.contains(it.instanceId) }
    }
}
package ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent

typealias MainNavigatorFactory = (instanceId: String) -> Navigator

typealias NestedNavigatorFactory = (instanceId: String, parentInEventsChannel: Channel<NavigatorEventsChannelEvent>) -> Navigator

fun interface NavigatorFactory {

    fun create(
        params: NavigatorFactoryParams
    ): Navigator
}
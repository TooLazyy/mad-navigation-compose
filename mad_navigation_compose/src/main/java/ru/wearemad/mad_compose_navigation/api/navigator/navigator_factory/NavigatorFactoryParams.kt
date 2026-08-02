package ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent

sealed class NavigatorFactoryParams {

    class MainNavigatorParams(
        val instanceId: String,
    ) : NavigatorFactoryParams()

    class NestedNavigatorParams(
        val instanceId: String,
        val parentInEventsChannel: Channel<NavigatorEventsChannelEvent>
    ) : NavigatorFactoryParams()
}
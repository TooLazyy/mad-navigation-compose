package ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent

sealed class NavigatorFactoryParams {

    class MainNavigatorParams(
        val screenId: String,
    ) : NavigatorFactoryParams()

    class NestedNavigatorParams(
        val screenId: String,
        val parentInEventsChannel: Channel<NavigatorEventsChannelEvent>
    ) : NavigatorFactoryParams()
}
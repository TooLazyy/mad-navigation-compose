package ru.wearemad.mad_compose_navigation.api.restorer

import android.os.Bundle
import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory

class NavigatorRestorerParams(
    val state: Bundle,
    val screenId: String,
    val factory: NavigatorFactory,
    val parentInEventsChannel: Channel<NavigatorEventsChannelEvent>
)
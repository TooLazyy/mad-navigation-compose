package ru.wearemad.mad_compose_navigation.api.navigator.data

import kotlinx.coroutines.channels.Channel

interface NavigatorEventsChannelProvider {

    val inEventsChannel: Channel<NavigatorEventsChannelEvent>

    val outEventsChannel: Channel<NavigatorEventsChannelEvent>?
}
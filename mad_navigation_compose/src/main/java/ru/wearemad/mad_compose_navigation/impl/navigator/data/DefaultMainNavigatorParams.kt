package ru.wearemad.mad_compose_navigation.impl.navigator.data

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.back_press.BackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.back_press.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.impl.back_press.ChildrenBackPressHandlerDelegate
import ru.wearemad.mad_compose_navigation.impl.back_press.DefaultBackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.navigator.data.MainNavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.impl.navigator.nested_host.DefaultNestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.restorer.MainNavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.saver.MainNavigatorSaver
import ru.wearemad.mad_compose_navigation.impl.restorer.DefaultMainNavigatorRestorer
import ru.wearemad.mad_compose_navigation.impl.saver.DefaultMainNavigatorSaver

class DefaultMainNavigatorParams(
    canGoBack: Boolean = false,
    override val screenId: String,
    override val saver: MainNavigatorSaver = DefaultMainNavigatorSaver(),
    override val restorer: MainNavigatorRestorer = DefaultMainNavigatorRestorer(),
    override val childrenBackPressHandler: ChildrenBackPressHandler = ChildrenBackPressHandlerDelegate(),
    override val inEventsChannel: Channel<NavigatorEventsChannelEvent> = Channel(capacity = Channel.BUFFERED),
    override val nestedNavigatorsHost: NestedNavigatorsHost = DefaultNestedNavigatorsHost(inEventsChannel),
    override val backPressCallbackHolder2: BackPressCallbackHolder = DefaultBackPressCallbackHolder(
        canGoBack = canGoBack,
        inEventsChannel = inEventsChannel
    )
) : MainNavigatorParams {

    override val outEventsChannel: Channel<NavigatorEventsChannelEvent>? = null
}
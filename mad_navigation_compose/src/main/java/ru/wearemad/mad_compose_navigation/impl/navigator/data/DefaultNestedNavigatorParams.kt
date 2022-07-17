package ru.wearemad.mad_compose_navigation.impl.navigator.data

import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.back_press.BackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.back_press.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.impl.back_press.ChildrenBackPressHandlerDelegate
import ru.wearemad.mad_compose_navigation.impl.back_press.DefaultBackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent
import ru.wearemad.mad_compose_navigation.api.navigator.data.NestedNavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.impl.navigator.nested_host.DefaultNestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.restorer.NestedNavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.saver.NestedNavigatorSaver
import ru.wearemad.mad_compose_navigation.impl.restorer.DefaultNestedNavigatorRestorer
import ru.wearemad.mad_compose_navigation.impl.saver.DefaultNestedNavigatorSaver

class DefaultNestedNavigatorParams(
    canGoBack: Boolean = false,
    override val screenId: String,
    override val outEventsChannel: Channel<NavigatorEventsChannelEvent>,
    override val saver: NestedNavigatorSaver = DefaultNestedNavigatorSaver(),
    override val restorer: NestedNavigatorRestorer = DefaultNestedNavigatorRestorer(),
    override val childrenBackPressHandler: ChildrenBackPressHandler = ChildrenBackPressHandlerDelegate(),
    override val inEventsChannel: Channel<NavigatorEventsChannelEvent> = Channel(capacity = Channel.BUFFERED),
    override val nestedNavigatorsHost: NestedNavigatorsHost = DefaultNestedNavigatorsHost(inEventsChannel),
    override val backPressCallbackHolder2: BackPressCallbackHolder = DefaultBackPressCallbackHolder(
        canGoBack = canGoBack,
        inEventsChannel = inEventsChannel
    )
) : NestedNavigatorParams
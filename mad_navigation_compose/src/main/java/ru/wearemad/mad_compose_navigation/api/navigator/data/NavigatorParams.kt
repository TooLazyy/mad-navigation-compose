package ru.wearemad.mad_compose_navigation.api.navigator.data

import ru.wearemad.mad_compose_navigation.api.back_press.BackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.back_press.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.restorer.MainNavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.restorer.NavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.restorer.NestedNavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.saver.MainNavigatorSaver
import ru.wearemad.mad_compose_navigation.api.saver.NavigatorSaver
import ru.wearemad.mad_compose_navigation.api.saver.NestedNavigatorSaver

interface NavigatorParams : NavigatorEventsChannelProvider {

    val screenId: String

    val saver: NavigatorSaver

    val restorer: NavigatorRestorer

    val childrenBackPressHandler: ChildrenBackPressHandler

    val nestedNavigatorsHost: NestedNavigatorsHost

    val backPressCallbackHolder2: BackPressCallbackHolder
}

interface MainNavigatorParams : NavigatorParams {

    override val saver: MainNavigatorSaver

    override val restorer: MainNavigatorRestorer
}

interface NestedNavigatorParams : NavigatorParams {

    override val saver: NestedNavigatorSaver

    override val restorer: NestedNavigatorRestorer
}
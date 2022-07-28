package ru.wearemad.mad_compose_navigation.utils

import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.MainNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.impl.navigator.AppMainNavigator
import ru.wearemad.mad_compose_navigation.impl.navigator.AppNestedNavigator
import ru.wearemad.mad_compose_navigation.impl.navigator.data.DefaultMainNavigatorParams
import ru.wearemad.mad_compose_navigation.impl.navigator.data.DefaultNestedNavigatorParams

fun defaultMainNavigatorFactory(
    canGoBack: Boolean = false
): MainNavigatorFactory = {
    AppMainNavigator(
        DefaultMainNavigatorParams(
            canGoBack = canGoBack,
            screenId = it
        )
    )
}

fun defaultNestedNavigatorFactory(
    canGoBack: Boolean = false
): NestedNavigatorFactory = { screenId, parentInEventChannel ->
    AppNestedNavigator(
        DefaultNestedNavigatorParams(
            canGoBack = canGoBack,
            screenId = screenId,
            outEventsChannel = parentInEventChannel
        )
    )
}
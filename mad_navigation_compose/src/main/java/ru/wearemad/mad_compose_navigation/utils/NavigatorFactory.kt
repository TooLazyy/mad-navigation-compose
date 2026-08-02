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
            instanceId = it
        )
    )
}

fun defaultNestedNavigatorFactory(
    canGoBack: Boolean = false
): NestedNavigatorFactory = { instanceId, parentInEventChannel ->
    AppNestedNavigator(
        DefaultNestedNavigatorParams(
            canGoBack = canGoBack,
            instanceId = instanceId,
            outEventsChannel = parentInEventChannel
        )
    )
}
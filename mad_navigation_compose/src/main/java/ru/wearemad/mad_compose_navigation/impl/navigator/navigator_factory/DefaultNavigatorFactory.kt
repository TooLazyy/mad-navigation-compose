package ru.wearemad.mad_compose_navigation.impl.navigator.navigator_factory

import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.MainNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactoryParams
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory

class DefaultNavigatorFactory(
    private val mainFactory: MainNavigatorFactory,
    private val nestedFactory: NestedNavigatorFactory,
) : NavigatorFactory {

    override fun create(
        params: NavigatorFactoryParams
    ): Navigator = when (params) {
        is NavigatorFactoryParams.MainNavigatorParams -> mainFactory(params.screenId)
        is NavigatorFactoryParams.NestedNavigatorParams -> nestedFactory(params.screenId, params.parentInEventsChannel)
    }
}
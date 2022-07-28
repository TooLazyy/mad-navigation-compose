package ru.wearemad.mad_compose_navigation.impl.router

import ru.wearemad.mad_compose_navigation.api.router.RouterProvidersHolder

/**
 * Default implementation of RouterProvidersHolder for Router class
 */
class DefaultRouterProvidersHolder : RouterProvidersHolder<Router>(
    { Router() }
)
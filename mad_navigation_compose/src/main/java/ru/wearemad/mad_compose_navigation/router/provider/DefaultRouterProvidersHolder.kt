package ru.wearemad.mad_compose_navigation.router.provider

import ru.wearemad.mad_compose_navigation.router.Router

/**
 * Default implementation of RouterProvidersHolder for Router class
 */
class DefaultRouterProvidersHolder : RouterProvidersHolder<Router>(
    { Router() }
)
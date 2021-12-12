package ru.wearemad.mad_compose_navigation.router.provider

import ru.wearemad.mad_compose_navigation.router.Router
import ru.wearemad.mad_compose_navigation.router.holder.RouterNavigatorHolder

/**
 * Class to hold all sub routers
 * The main idea is to keep application's root router as singleton and
 * all flow routers within RouterProvidersHolder to be able to clean them up when needed
 */
abstract class RouterProvidersHolder<R : Router>(
    private val routerFactory: () -> R
) {

    private val routerProvidersMap: MutableMap<String, RouterProvider<R>> = mutableMapOf()

    fun getOrCreateRouter(key: String): R = getOrCreateRouterProvider(key).router

    fun getRouterOrNull(key: String): R? = routerProvidersMap[key]?.router

    fun getOrCreateHolder(key: String): RouterNavigatorHolder =
        getOrCreateRouterProvider(key).getHolder()

    fun remove(key: String) {
        routerProvidersMap.remove(key)
    }

    private fun getOrCreateRouterProvider(key: String): RouterProvider<R> =
        routerProvidersMap.getOrPut(key) {
            RouterProvider.create(routerFactory())
        }
}
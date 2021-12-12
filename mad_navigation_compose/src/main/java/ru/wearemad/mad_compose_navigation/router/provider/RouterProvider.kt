package ru.wearemad.mad_compose_navigation.router.provider

import ru.wearemad.mad_compose_navigation.router.Router
import ru.wearemad.mad_compose_navigation.router.holder.RouterNavigatorHolder

/**
 * ROuter provider which allows to keep together router and it's holder
 */
class RouterProvider<R : Router>(val router: R) {

    companion object {

        /**
         * Creates a default instance
         */
        fun create(): RouterProvider<Router> = RouterProvider(Router())

        /**
         * Creates a custom instance
         */
        fun <R : Router> create(router: R): RouterProvider<R> = RouterProvider(router)
    }

    fun getHolder(): RouterNavigatorHolder = router.holder
}
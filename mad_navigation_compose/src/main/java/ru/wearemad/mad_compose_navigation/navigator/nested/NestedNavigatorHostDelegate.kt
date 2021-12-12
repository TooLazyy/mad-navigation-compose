package ru.wearemad.mad_compose_navigation.navigator.nested

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.base.NavigatorState
import ru.wearemad.mad_compose_navigation.route.Route

/**
 * Default implementation of NestedNavigatorsHost
 */
class NestedNavigatorHostDelegate : NestedNavigatorsHost {

    override var nestedNavigators: List<NestedNavigatorData> = listOf()

    override var onNestedNavigatorStackChanged: (screenKey: String, state: NavigatorState) -> Unit = { _, _ -> }

    override fun getOrCreateNestedNavigator(
        screenKey: String,
        factory: () -> NestedNavigator
    ): NestedNavigator {
        val index = nestedNavigators.indexOfFirst { it.screenKey == screenKey }
        if (index >= 0) {
            return nestedNavigators[index].navigator
        }
        return createAndGetNestedRouter(
            screenKey,
            factory
        )
    }

    override fun findNestedNavigator(
        screenKey: String
    ): NestedNavigator? = nestedNavigators
        .find { it.screenKey == screenKey }
        ?.navigator

    override fun removeNestedNavigator(screenKey: String) {
        val index = nestedNavigators.indexOfFirst { it.screenKey == screenKey }
        if (index < 0) {
            return
        }
        nestedNavigators = nestedNavigators.toMutableList().apply {
            removeAt(index)
        }
    }

    override fun prepareNestedNavigatorSavedState(): Bundle = Bundle().apply {
        nestedNavigators.forEach { nestedRouter ->
            val key = nestedRouter.screenKey
            val savedState = nestedRouter.navigator.saveState()
            putBundle(key, savedState)
        }
    }

    override fun restoreNestedNavigators(
        inState: Bundle,
        navigatorFactory: () -> NestedNavigator,
        nestedNavigatorFactory: () -> NestedNavigator,
    ) {
        val nestedNavigatorsBundle =
            inState
                .getBundle(Navigator.KEY_COMPOSE_NESTED_NAVIGATORS_DATA) ?: return
        nestedNavigatorsBundle.keySet()
            .mapNotNull { key ->
                val bundle = nestedNavigatorsBundle.getBundle(key)
                if (bundle == null) {
                    null
                } else {
                    key to bundle
                }
            }
            .forEach { bundlePair ->
                createAndGetNestedRouter(
                    bundlePair.first,
                    nestedNavigatorFactory
                ).also {
                    it.restoreState(bundlePair.second, nestedNavigatorFactory)
                }
            }
    }

    override fun clearUnusedNestedNavigators(routesList: List<Route>) {
        val routesListKeys = routesList
            .map { it.screenKey }
        nestedNavigators.map { it.screenKey }
            .filterNot { routesListKeys.contains(it) }
            .forEach {
                removeNestedNavigator(it)
            }
    }

    private fun createAndGetNestedRouter(
        screenKey: String,
        factory: () -> NestedNavigator
    ): NestedNavigator {
        val nestedNavigator = factory().also { navigator ->
            navigator.addNavigatorStateChangedListener {
                onNestedNavigatorStackChanged(screenKey, it)
            }
        }
        nestedNavigators = nestedNavigators.toMutableList().apply {
            add(
                NestedNavigatorData(
                    screenKey,
                    nestedNavigator
                )
            )
        }
        return nestedNavigator
    }
}
package ru.wearemad.mad_compose_navigation.navigator.nested

import android.os.Bundle
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
        nestedNavigators.forEach { nestedData ->
            val key = nestedData.screenKey
            val savedState = nestedData.navigator.saveState(key)
            putBundle(key, savedState)
        }
    }

    override suspend fun restoreNestedNavigators(
        inState: Bundle?,
        navigatorFactory: () -> NestedNavigator,
        nestedNavigatorFactory: () -> NestedNavigator,
    ) {
        val nestedNavigatorsBundle = inState ?: return
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
                val navigator = createAndGetNestedRouter(
                    bundlePair.first,
                    nestedNavigatorFactory
                )
                navigator.restoreState(bundlePair.second, bundlePair.first, nestedNavigatorFactory)
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
        val stackChangedListener = NestedNavigatorStateChangedListener {
            onNestedNavigatorStackChanged(screenKey, it)
        }
        val nestedNavigator = factory().also { navigator ->
            navigator.addNavigatorStateChangedListener(stackChangedListener)
        }
        nestedNavigators = nestedNavigators.toMutableList().apply {
            add(
                NestedNavigatorData(
                    screenKey,
                    nestedNavigator
                )
            )
        }
        stackChangedListener.onChanged(nestedNavigator.navigatorStateFlow.value)
        return nestedNavigator
    }
}
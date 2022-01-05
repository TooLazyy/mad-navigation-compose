package ru.wearemad.mad_compose_navigation.navigator

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorStateChangedListener
import ru.wearemad.mad_compose_navigation.route.Route

open class AppNestedNavigator(
    canGoBack: Boolean = false
) : CommonNavigator(canGoBack = canGoBack),
    NestedNavigator {

    companion object {

        private const val KEY_NESTED_NAVIGATOR_DATA_PREFIX = "key_nested_navigator_data_prefix_"
        private const val KEY_NESTED_NAVIGATOR_NESTED_DATA_PREFIX = "key_nested_navigator_nested_data_prefix_"
    }

    private val stateChangedListeners: MutableSet<NestedNavigatorStateChangedListener> = mutableSetOf()

    override fun addNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener) {
        stateChangedListeners.add(listener)
    }

    override fun removeNavigatorStateChangedListener(listener: NestedNavigatorStateChangedListener) {
        stateChangedListeners.remove(listener)
    }

    override fun afterStackChanged() {
        stateChangedListeners.forEach {
            it.onChanged(navigatorState)
        }
    }

    override fun saveState(screenId: String): Bundle = Bundle().apply {
        putParcelableArray(
            buildRoutesKey(screenId),
            routesList.toTypedArray()
        )
        putBundle(
            buildNestedKey(screenId),
            prepareNestedNavigatorSavedState()
        )
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun restoreState(
        inState: Bundle,
        screenId: String,
        nestedNavigatorFactory: () -> NestedNavigator
    ) {
        routesList =
            (inState.getParcelableArray(buildRoutesKey(screenId))
                ?: arrayOf()).toList() as List<Route>

        val nestedNavigatorsBundle = inState.getBundle(
            buildNestedKey(screenId)
        )
        restoreNestedNavigators(
            nestedNavigatorsBundle,
            nestedNavigatorFactory,
            nestedNavigatorFactory
        )
        onStackChanged()
    }

    private fun buildNestedKey(screenId: String): String =
        KEY_NESTED_NAVIGATOR_NESTED_DATA_PREFIX + screenId

    private fun buildRoutesKey(screenId: String): String =
        KEY_NESTED_NAVIGATOR_DATA_PREFIX + screenId
}
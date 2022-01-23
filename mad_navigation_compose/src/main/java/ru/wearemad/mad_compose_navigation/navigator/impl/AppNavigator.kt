package ru.wearemad.mad_compose_navigation.navigator.impl

import android.os.Bundle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.navigator.base.CommonNavigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.route.Route

open class AppNavigator(
    canGoBack: Boolean = false
) : CommonNavigator(canGoBack = canGoBack),
    RootNavigator {

    companion object {

        private const val KEY_COMPOSE_ROUTES_DATA = "key_compose_routes_data"
        private const val KEY_COMPOSE_DIALOGS_DATA = "key_compose_dialogs_data"
        private const val KEY_COMPOSE_NESTED_NAVIGATORS_DATA = "key_root_nested_navigators_data"
    }

    private var restoreStateJob: Job? = null

    @Suppress("UNCHECKED_CAST")
    override fun restoreState(
        inState: Bundle,
        nestedNavigatorFactory: () -> NestedNavigator
    ) {
        restoreStateJob?.cancel()
        restoreStateJob = scope.launch {
            routesList =
                (inState.getParcelableArray(KEY_COMPOSE_ROUTES_DATA) ?: arrayOf()).toList() as List<Route>
            dialogRoutesList =
                (inState.getParcelableArray(KEY_COMPOSE_DIALOGS_DATA) ?: arrayOf()).toList() as List<Route>

            val nestedNavigatorsBundle = inState.getBundle(KEY_COMPOSE_NESTED_NAVIGATORS_DATA)
            restoreNestedNavigators(
                nestedNavigatorsBundle,
                nestedNavigatorFactory,
                nestedNavigatorFactory
            )
            onStackChanged()
            afterStackChanged()
        }
    }

    override fun saveState(): Bundle = Bundle().apply {
        putParcelableArray(
            KEY_COMPOSE_ROUTES_DATA,
            routesList.toTypedArray()
        )
        putParcelableArray(
            KEY_COMPOSE_DIALOGS_DATA,
            dialogRoutesList.toTypedArray()
        )
        putBundle(
            KEY_COMPOSE_NESTED_NAVIGATORS_DATA,
            prepareNestedNavigatorSavedState()
        )
    }
}
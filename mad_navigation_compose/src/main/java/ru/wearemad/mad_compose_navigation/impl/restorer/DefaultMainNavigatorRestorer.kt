package ru.wearemad.mad_compose_navigation.impl.restorer

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactoryParams
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.api.restorer.MainNavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.restorer.NavigatorRestorerParams
import ru.wearemad.mad_compose_navigation.api.restorer.RestoreStateResult
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_BUNDLE
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_DIALOGS_DATA
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATORS_DATA
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_ROUTES_DATA

class DefaultMainNavigatorRestorer : MainNavigatorRestorer {

    @Suppress("UNCHECKED_CAST")
    override fun restore(
        params: NavigatorRestorerParams
    ): RestoreStateResult {
        val mainBundle = params.state.getBundle(KEY_COMPOSE_MAIN_NAVIGATOR_BUNDLE) ?: return RestoreStateResult()
        val currentStack =
            (mainBundle.getParcelableArray(KEY_COMPOSE_MAIN_NAVIGATOR_ROUTES_DATA) ?: arrayOf()).toList() as List<Route>
        val currentDialogsStack =
            (mainBundle.getParcelableArray(KEY_COMPOSE_MAIN_NAVIGATOR_DIALOGS_DATA)
                ?: arrayOf()).toList() as List<Route>
        val nestedNavigators = restoreNestedNavigators(mainBundle, params)
        return RestoreStateResult(
            currentStack = currentStack,
            currentDialogsStack = currentDialogsStack,
            nestedNavigators = nestedNavigators
        )
    }

    private fun restoreNestedNavigators(
        mainBundle: Bundle,
        params: NavigatorRestorerParams
    ): List<NestedNavigatorData> {
        val nestedNavigatorsBundle =
            mainBundle.getBundle(KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATORS_DATA) ?: return listOf()
        return nestedNavigatorsBundle
            .keySet()
            .mapNotNull { key ->
                val realKey = key.replace(KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX, "")
                val nestedBundle = nestedNavigatorsBundle.getBundle(key)
                if (nestedBundle == null) {
                    null
                } else {
                    realKey to nestedBundle
                }
            }
            .map { (screenId, bundle) ->
                val navigator = params.factory.create(
                    NavigatorFactoryParams.NestedNavigatorParams(
                        screenId,
                        params.parentInEventsChannel
                    )
                )
                navigator.restoreState(bundle, params.factory)
                NestedNavigatorData(
                    screenId,
                    navigator
                )
            }
    }
}
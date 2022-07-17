package ru.wearemad.mad_compose_navigation.impl.restorer

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactoryParams
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.api.restorer.NavigatorRestorerParams
import ru.wearemad.mad_compose_navigation.api.restorer.NestedNavigatorRestorer
import ru.wearemad.mad_compose_navigation.api.restorer.RestoreStateResult
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_NESTED_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX
import ru.wearemad.mad_compose_navigation.impl.buildBundlePrefix
import ru.wearemad.mad_compose_navigation.impl.buildDialogsKey
import ru.wearemad.mad_compose_navigation.impl.buildNestedKey
import ru.wearemad.mad_compose_navigation.impl.buildRoutesKey

class DefaultNestedNavigatorRestorer : NestedNavigatorRestorer {

    @Suppress("UNCHECKED_CAST")
    override fun restore(
        params: NavigatorRestorerParams
    ): RestoreStateResult {
        val screenId = params.screenId
        val mainBundle = params.state.getBundle(buildBundlePrefix(screenId)) ?: return RestoreStateResult()
        val currentStack =
            (mainBundle.getParcelableArray(buildRoutesKey(screenId)) ?: arrayOf()).toList() as List<Route>
        val currentDialogsStack =
            (mainBundle
                .getParcelableArray(buildDialogsKey(screenId)) ?: arrayOf()).toList() as List<Route>

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
            mainBundle.getBundle(buildNestedKey(params.screenId)) ?: return listOf()
        return nestedNavigatorsBundle
            .keySet()
            .mapNotNull { key ->
                val realKey = key.replace(KEY_COMPOSE_NESTED_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX, "")
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
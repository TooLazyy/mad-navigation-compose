package ru.wearemad.mad_compose_navigation.impl.saver

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.saver.MainNavigatorSaver
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_BUNDLE
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_DIALOGS_DATA
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATORS_DATA
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX
import ru.wearemad.mad_compose_navigation.impl.KEY_COMPOSE_MAIN_NAVIGATOR_ROUTES_DATA

class DefaultMainNavigatorSaver : MainNavigatorSaver {

    override fun save(
        navigatorState: NavigatorState,
        nestedNavigators: List<NestedNavigatorData>,
        input: Bundle
    ) {
        val result = Bundle().apply {
            putParcelableArray(
                KEY_COMPOSE_MAIN_NAVIGATOR_ROUTES_DATA,
                navigatorState.currentStack.toTypedArray()
            )
            putParcelableArray(
                KEY_COMPOSE_MAIN_NAVIGATOR_DIALOGS_DATA,
                navigatorState.currentDialogsStack.toTypedArray()
            )
            putBundle(
                KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATORS_DATA,
                buildNestedNavigatorsBundle(nestedNavigators)
            )
        }
        input.putBundle(KEY_COMPOSE_MAIN_NAVIGATOR_BUNDLE, result)
    }

    private fun buildNestedNavigatorsBundle(
        nestedNavigators: List<NestedNavigatorData>,
    ): Bundle = Bundle().apply {
        nestedNavigators.forEach { nestedNavigatorData ->
            val nestedBundle = nestedNavigatorData.navigator.saveState()
            putBundle(
                KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX + nestedNavigatorData.screenKey,
                nestedBundle
            )
        }
    }
}
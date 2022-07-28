package ru.wearemad.mad_compose_navigation.impl.saver

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData
import ru.wearemad.mad_compose_navigation.api.saver.NestedNavigatorSaver
import ru.wearemad.mad_compose_navigation.impl.buildBundlePrefix
import ru.wearemad.mad_compose_navigation.impl.buildDialogsKey
import ru.wearemad.mad_compose_navigation.impl.buildNestedKey
import ru.wearemad.mad_compose_navigation.impl.buildNestedNavigatorDataKey
import ru.wearemad.mad_compose_navigation.impl.buildRoutesKey

class DefaultNestedNavigatorSaver : NestedNavigatorSaver {

    override fun save(
        navigatorState: NavigatorState,
        nestedNavigators: List<NestedNavigatorData>,
        input: Bundle
    ) {
        val screenId = navigatorState.screenId
        val result = Bundle().apply {
            putParcelableArray(
                buildRoutesKey(screenId),
                navigatorState.currentStack.toTypedArray()
            )
            putParcelableArray(
                buildDialogsKey(screenId),
                navigatorState.currentDialogsStack.toTypedArray()
            )
            putBundle(
                buildNestedKey(screenId),
                buildNestedNavigatorsBundle(nestedNavigators)
            )
        }
        input.putBundle(
            buildBundlePrefix(screenId),
            result
        )
    }

    private fun buildNestedNavigatorsBundle(
        nestedNavigators: List<NestedNavigatorData>,
    ): Bundle = Bundle().apply {
        nestedNavigators.forEach { nestedNavigatorData ->
            val nestedBundle = nestedNavigatorData.navigator.saveState()
            putBundle(
                buildNestedNavigatorDataKey(nestedNavigatorData.screenKey),
                nestedBundle
            )
        }
    }
}
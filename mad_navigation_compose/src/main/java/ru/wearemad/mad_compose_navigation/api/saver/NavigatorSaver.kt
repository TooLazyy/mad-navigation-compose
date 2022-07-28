package ru.wearemad.mad_compose_navigation.api.saver

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.nested.data.NestedNavigatorData

interface NavigatorSaver {

    fun save(
        navigatorState: NavigatorState,
        nestedNavigators: List<NestedNavigatorData>,
        input: Bundle
    )
}

interface MainNavigatorSaver : NavigatorSaver

interface NestedNavigatorSaver : NavigatorSaver
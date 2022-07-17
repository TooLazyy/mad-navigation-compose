package ru.wearemad.mad_compose_navigation.api.navigator.nested.data

import ru.wearemad.mad_compose_navigation.api.navigator.Navigator

data class NestedNavigatorData(
    val screenKey: String,
    val navigator: Navigator
)
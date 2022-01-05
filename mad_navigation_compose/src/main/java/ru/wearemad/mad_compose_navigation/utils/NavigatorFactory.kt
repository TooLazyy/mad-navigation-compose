package ru.wearemad.mad_compose_navigation.utils

import ru.wearemad.mad_compose_navigation.navigator.impl.AppNavigator
import ru.wearemad.mad_compose_navigation.navigator.impl.AppNestedNavigator
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator

fun createAppNavigator(
    canGoBack: Boolean = false
): Navigator = AppNavigator(canGoBack)

fun createAppNestedNavigator(
    canGoBack: Boolean = false
): NestedNavigator = AppNestedNavigator(canGoBack)
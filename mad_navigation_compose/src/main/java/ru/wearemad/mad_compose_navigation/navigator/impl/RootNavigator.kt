package ru.wearemad.mad_compose_navigation.navigator.impl

import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.navigator.saveable.RestorableNavigator
import ru.wearemad.mad_compose_navigation.navigator.saveable.SaveableNavigator

/**
 * Interface for a root navigator instance
 */
interface RootNavigator :
    Navigator,
    RestorableNavigator,
    SaveableNavigator
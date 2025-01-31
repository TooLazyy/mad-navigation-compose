package ru.wearemad.mad_compose_navigation.api.navigator

import kotlinx.coroutines.flow.StateFlow
import ru.wearemad.mad_compose_navigation.api.back_press.BackPressHandler
import ru.wearemad.mad_compose_navigation.api.back_press.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.api.command.CommandsExecutor
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.restorer.RestorableState
import ru.wearemad.mad_compose_navigation.api.saver.SavableState

interface Navigator :
    CommandsExecutor,
    NavigatorScope,
    SavableState,
    RestorableState,
    BackPressHandler,
    ChildrenBackPressHandler,
    NestedNavigatorsHost,
    AnimationStateUpdater {

    val state: NavigatorState

    val stateFlow: StateFlow<NavigatorState>
}

fun interface AnimationStateUpdater {

    fun updateAnimationState(animationInProgress: Boolean)
}
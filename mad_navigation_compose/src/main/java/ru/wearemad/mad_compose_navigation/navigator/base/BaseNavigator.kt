package ru.wearemad.mad_compose_navigation.navigator.base

import android.os.Bundle
import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wearemad.mad_compose_navigation.back_press.handler.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.back_press.handler.ChildrenBackPressHandlerDelegate
import ru.wearemad.mad_compose_navigation.command.Back
import ru.wearemad.mad_compose_navigation.command.Command
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigator
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorHostDelegate
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorState
import ru.wearemad.mad_compose_navigation.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.route.Route

abstract class BaseNavigator :
    Navigator,
    NestedNavigatorsHost by NestedNavigatorHostDelegate(),
    ChildrenBackPressHandler by ChildrenBackPressHandlerDelegate() {

    private val supervisorJob = SupervisorJob()
    private val bgDispatcher = Dispatchers.IO
    private val mainDispatcher = Dispatchers.Main.immediate

    private val eventsChannel = Channel<NavigatorEvent>()

    protected var routesList = listOf<Route>()
    protected var navigatorState: NavigatorState = NavigatorState()
    protected val navigatorMutableStateFlow = MutableStateFlow(navigatorState)

    private val scope = CoroutineScope(Dispatchers.Main.immediate + supervisorJob)

    override val navigatorStateFlow: StateFlow<NavigatorState>
        get() = navigatorMutableStateFlow

    override val canGoBack: Boolean
        get() = routesList.size > 1

    init {
        scope.launch(bgDispatcher) {
            eventsChannel.receiveAsFlow()
                .collect {
                    when (it) {
                        is NavigatorEvent.ExecuteCommands -> onExecuteCommandsEvent(it)
                        is NavigatorEvent.RestoreState -> onRestoreStateEvent(it)
                        is NavigatorEvent.OnNestedNavigatorsStackChanged -> onNestedNavigatorsStackChangedEvent()
                    }
                }
        }
        onNestedNavigatorStackChanged = { id, state ->
            scope.launch {
                eventsChannel.send(NavigatorEvent.OnNestedNavigatorsStackChanged)
            }
        }
    }

    override fun cancelJobs() {
        supervisorJob.cancelChildren()
    }

    @MainThread
    override suspend fun executeCommands(vararg commands: Command) {
        eventsChannel.send(NavigatorEvent.ExecuteCommands(commands))
    }

    override fun saveState(): Bundle =
        Bundle().apply {
            putParcelableArray(
                Navigator.KEY_COMPOSE_NAVIGATORS_DATA,
                routesList.toTypedArray()
            )
            putBundle(
                Navigator.KEY_COMPOSE_NESTED_NAVIGATORS_DATA,
                prepareNestedNavigatorSavedState()
            )
        }

    @Suppress("UNCHECKED_CAST")
    override fun restoreState(
        inState: Bundle,
        nestedNavigatorFactory: () -> NestedNavigator
    ) {
        scope.launch {
            eventsChannel.send(NavigatorEvent.RestoreState(inState, nestedNavigatorFactory))
        }
    }

    override fun dispatchSystemOnBackPressed() {
        if (canGoBack.not()) {
            return
        }

        val dispatched = dispatchBackPressedToChildren()
        if (dispatched) {
            return
        }

        scope.launch {
            executeCommands(Back())
        }
    }

    open fun afterStackChanged() {}

    private suspend fun onNestedNavigatorsStackChangedEvent() {
        navigatorState = navigatorState.copy(
            nestedNavigatorsState = nestedNavigators.map {
                NestedNavigatorState(
                    it.screenKey,
                    it.navigator.navigatorStateFlow.value,
                )
            }
        )
        clearUnusedNestedNavigators(routesList)
        withContext(mainDispatcher) {
            updateNavigatorState()
        }
    }

    private suspend fun onExecuteCommandsEvent(event: NavigatorEvent.ExecuteCommands) {
        event.commands.forEach {
            routesList = it.execute(routesList)
        }
        onStackChanged()
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun onRestoreStateEvent(event: NavigatorEvent.RestoreState) {
        val inState = event.inState
        val nestedNavigatorFactory = event.nestedNavigatorFactory

        routesList =
            (inState.getParcelableArray(Navigator.KEY_COMPOSE_NAVIGATORS_DATA) ?: arrayOf())
                .toList() as List<Route>
        restoreNestedNavigators(inState, nestedNavigatorFactory, nestedNavigatorFactory)
        onStackChanged()
    }

    private suspend fun onStackChanged() {
        navigatorState = NavigatorState(
            currentRoute = routesList.lastOrNull(),
            currentStack = routesList,
            nestedNavigatorsState = nestedNavigators.map {
                NestedNavigatorState(
                    it.screenKey,
                    it.navigator.navigatorStateFlow.value,
                )
            },
            stateChangedAtLeastOnce = true,
        )
        clearUnusedNestedNavigators(routesList)
        withContext(mainDispatcher) {
            updateNavigatorState()
        }
    }

    @MainThread
    private fun updateNavigatorState() {
        updateOnBackPressedCallback()
        navigatorMutableStateFlow.value = navigatorState
        afterStackChanged()
    }
}
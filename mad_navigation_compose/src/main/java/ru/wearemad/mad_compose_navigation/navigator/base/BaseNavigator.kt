package ru.wearemad.mad_compose_navigation.navigator.base

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

    protected val scope = CoroutineScope(Dispatchers.Main.immediate + supervisorJob)

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

    protected suspend fun onStackChanged() {
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
            afterStackChanged()
        }
    }

    private suspend fun onExecuteCommandsEvent(event: NavigatorEvent.ExecuteCommands) {
        event.commands.forEach {
            routesList = it.execute(routesList)
        }
        onStackChanged()
        afterStackChanged()
    }

    @MainThread
    private fun updateNavigatorState() {
        updateOnBackPressedCallback()
        navigatorMutableStateFlow.value = navigatorState
    }
}
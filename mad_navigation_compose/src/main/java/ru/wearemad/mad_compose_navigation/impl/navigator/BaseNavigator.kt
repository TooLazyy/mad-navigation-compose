package ru.wearemad.mad_compose_navigation.impl.navigator

import android.os.Bundle
import android.util.Log
import androidx.annotation.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wearemad.mad_compose_navigation.api.back_press.BackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.back_press.ChildrenBackPressHandler
import ru.wearemad.mad_compose_navigation.api.command.Command
import ru.wearemad.mad_compose_navigation.api.command.CommandInput
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.nested.NestedNavigatorsHost
import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.impl.command.Back
import ru.wearemad.mad_compose_navigation.impl.navigator.data.DispatchSystemOnBackPressed
import ru.wearemad.mad_compose_navigation.impl.navigator.data.NestedStackChanged
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

abstract class BaseNavigator(
    protected val params: NavigatorParams
) : Navigator,
    ChildrenBackPressHandler by params.childrenBackPressHandler,
    NestedNavigatorsHost by params.nestedNavigatorsHost,
    BackPressCallbackHolder by params.backPressCallbackHolder2 {

    //coroutines
    private val supervisorJob = SupervisorJob()
    private val bgDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val mainDispatcher = Dispatchers.Main.immediate

    //state
    protected var routesList = listOf<Route>()
    protected var dialogRoutesList = listOf<Route>()

    //events/state flow
    private val stateMutableFlow = MutableStateFlow(NavigatorState(params.screenId))
    private val inputEventsChannel = Channel<NavigatorInputEvent>(capacity = BUFFERED)

    override val state: NavigatorState
        get() = stateMutableFlow.value

    override val stateFlow: StateFlow<NavigatorState> = stateMutableFlow

    override val canGoBack: Boolean
        get() = routesList.size > 1 || dialogRoutesList.isNotEmpty()

    override val coroutineContext: CoroutineContext = supervisorJob + bgDispatcher

    init {
        Log.d("MIINE", "BaseNavigator init: $this")
        subscribeToInputEvents()
    }

    override suspend fun executeCommands(vararg commands: Command) {
        Log.d("MIINE", "router: $this, executeCommands: ${commands.size}")
        inputEventsChannel.send(NavigatorInputEvent.ExecuteCommands(commands))
    }

    override fun cancelJobs() {
        supervisorJob.cancelChildren()
    }

    override fun saveState(): Bundle {
        val output = Bundle()
        params.saver.save(
            state,
            nestedNavigators,
            output
        )
        return output
    }

    override fun dispatchSystemOnBackPressed() {
        if (canGoBack.not()) {
            return
        }

        val dispatched = dispatchBackPressedToChildren()
        if (dispatched) {
            return
        }

        launch {
            executeCommands(Back())
        }
    }

    open suspend fun afterStackChanged() {}

    protected suspend fun onStackChanged() {
        clearUnusedNestedNavigators(routesList)
        val navigatorState = createNavigatorState()
        withContext(mainDispatcher) {
            updateNavigatorState(navigatorState)
            afterStackChanged()
        }
    }

    private suspend fun onNestedNavigatorsStackChangedEvent() {
        onStackChanged()
    }

    protected fun createNavigatorState(): NavigatorState = NavigatorState(
        screenId = params.screenId,
        currentStack = routesList,
        currentDialogsStack = dialogRoutesList,
        nestedNavigatorsState = nestedNavigators.map {
            it.navigator.state
        },
    )

    @MainThread
    protected fun updateNavigatorState(newState: NavigatorState) {
        updateOnBackPressedCallback(canGoBack)
        stateMutableFlow.value = newState
    }

    private fun subscribeToInputEvents() {
        launch {
            inputEventsChannel
                .receiveAsFlow()
                .collect {
                    when (it) {
                        is NavigatorInputEvent.ExecuteCommands -> onExecuteCommandsEvent(it)
                    }
                }
        }
        launch {
            params
                .inEventsChannel
                .receiveAsFlow()
                .collect {
                    when (it) {
                        is NestedStackChanged -> onNestedNavigatorsStackChangedEvent()
                        is DispatchSystemOnBackPressed -> withContext(mainDispatcher) {
                            dispatchSystemOnBackPressed()
                        }
                    }
                }
        }
    }

    private suspend fun onExecuteCommandsEvent(event: NavigatorInputEvent.ExecuteCommands) {
        Log.d("MIINE", "router: $this, onExecuteCommandsEvent")
        fun doIt(): String = buildString {
            routesList.forEach {
                append(it)
                append("\n")
            }
        }
        event.commands.forEach {
            Log.d("MIINE", "before onExecuteCommands: $it, routes: ${doIt()}")
            val result = it.execute(
                CommandInput(
                    routesList,
                    dialogRoutesList
                )
            )
            dialogRoutesList = result.newDialogsStack
            routesList = result.newScreensStack
            Log.d("MIINE", "after onExecuteCommands: $it, routes: ${doIt()}")
        }
        Log.d("MIINE", "onExecuteCommands done, routes: ${doIt()}")
        onStackChanged()
    }
}

sealed class NavigatorInputEvent {

    class ExecuteCommands(
        val commands: Array<out Command>
    ) : NavigatorInputEvent()
}
package ru.wearemad.mad_compose_navigation.impl.navigator

import android.os.Bundle
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
import ru.wearemad.mad_compose_navigation.api.back_press.SystemBackPressedListener
import ru.wearemad.mad_compose_navigation.api.command.Command
import ru.wearemad.mad_compose_navigation.api.command.CommandInput
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.data.StackEntry
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
    protected var routesList = listOf<StackEntry>()
    protected var dialogRoutesList = listOf<StackEntry>()
    private var withAnimation = false

    //events/state flow
    private val stateMutableFlow = MutableStateFlow(NavigatorState(params.instanceId))
    private val inputEventsChannel = Channel<NavigatorInputEvent>(capacity = BUFFERED)

    override val state: NavigatorState
        get() = stateMutableFlow.value

    override val stateFlow: StateFlow<NavigatorState> = stateMutableFlow

    protected open val keepsLastScreen: Boolean = false

    override val canGoBack: Boolean
        get() = routesList.size > 1 || dialogRoutesList.isNotEmpty()

    override val coroutineContext: CoroutineContext = supervisorJob + bgDispatcher

    init {
        subscribeToInputEvents()
    }

    override suspend fun executeCommands(vararg commands: Command) {
        inputEventsChannel.send(NavigatorInputEvent.ExecuteCommands(commands))
    }

    override fun updateAnimationState(animationInProgress: Boolean) {
        launch(mainDispatcher) {
            inputEventsChannel.send(NavigatorInputEvent.UpdateAnimationState(inProgress = animationInProgress))
        }
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
        val dispatched = dispatchBackPressedToChildren()
        if (dispatched) {
            return
        }

        launch {
            inputEventsChannel.send(NavigatorInputEvent.SystemBack())
        }
    }

    override fun addSystemBackPressedListener(listener: SystemBackPressedListener) {
        params.childrenBackPressHandler.addSystemBackPressedListener(listener)
        updateOnBackPressedCallback(shouldInterceptBackPress())
    }

    override fun removeSystemBackPressedListener(listener: SystemBackPressedListener) {
        params.childrenBackPressHandler.removeSystemBackPressedListener(listener)
        updateOnBackPressedCallback(shouldInterceptBackPress())
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
        instanceId = params.instanceId,
        currentStack = routesList,
        currentDialogsStack = dialogRoutesList,
        nestedNavigatorsState = nestedNavigators.map {
            it.navigator.state
        },
        withAnimation = withAnimation,
    )

    @MainThread
    protected fun updateNavigatorState(newState: NavigatorState) {
        updateOnBackPressedCallback(shouldInterceptBackPress())
        stateMutableFlow.value = newState
    }

    private fun shouldInterceptBackPress(): Boolean = canGoBack || hasSystemBackPressedListeners

    private fun subscribeToInputEvents() {
        launch {
            inputEventsChannel
                .receiveAsFlow()
                .collect {
                    when (it) {
                        is NavigatorInputEvent.ExecuteCommands -> onExecuteCommandsEvent(it)
                        is NavigatorInputEvent.UpdateAnimationState -> {
                            withAnimation = it.inProgress
                            onStackChanged()
                        }
                        is NavigatorInputEvent.SystemBack -> onSystemBackEvent()
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

    private suspend fun onSystemBackEvent() {
        if (canGoBack.not()) {
            withContext(mainDispatcher) {
                dispatchBackPressToSystem()
            }
            return
        }
        onExecuteCommandsEvent(
            NavigatorInputEvent.ExecuteCommands(arrayOf(Back(withAnimation = true)))
        )
    }

    private suspend fun onExecuteCommandsEvent(event: NavigatorInputEvent.ExecuteCommands) {
        var newRoutesList = routesList
        var newDialogRoutesList = dialogRoutesList
        var newWithAnimation = withAnimation
        event.commands.forEach {
            val result = it.execute(
                CommandInput(
                    newRoutesList,
                    newDialogRoutesList
                )
            )
            newDialogRoutesList = result.newDialogsStack
            newRoutesList = result.newScreensStack
            newWithAnimation = result.withAnimation
        }
        if (keepsLastScreen && routesList.isNotEmpty() && newRoutesList.isEmpty()) {
            withContext(mainDispatcher) {
                dispatchBackPressToSystem()
            }
            return
        }
        routesList = newRoutesList
        dialogRoutesList = newDialogRoutesList
        withAnimation = newWithAnimation
        onStackChanged()
    }
}

sealed class NavigatorInputEvent {

    class ExecuteCommands(
        val commands: Array<out Command>
    ) : NavigatorInputEvent()

    class UpdateAnimationState(
        val inProgress: Boolean
    ) : NavigatorInputEvent()

    class SystemBack : NavigatorInputEvent()
}
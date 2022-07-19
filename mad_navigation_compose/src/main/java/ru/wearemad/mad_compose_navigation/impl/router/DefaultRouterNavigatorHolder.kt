package ru.wearemad.mad_compose_navigation.impl.router

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.api.command.Command
import ru.wearemad.mad_compose_navigation.api.command.CommandsExecutor
import ru.wearemad.mad_compose_navigation.api.router.RouterNavigatorHolder

/**
 * Default implementation of RouterNavigatorHolder
 * It listens for commands being sent to a Channel and 3executes 'em on main thread
 */
class DefaultRouterNavigatorHolder : RouterNavigatorHolder {

    private val mainDispatcher = Dispatchers.Main.immediate
    private val commandsChannel = Channel<Array<out Command>>(
        capacity = Channel.BUFFERED
    )

    private var commandsChannelJob: Job? = null
    private var currentExecutor: CommandsExecutor? = null

    override suspend fun executeCommands(vararg commands: Command) {
        Log.d("MIINE", "DefaultRouterNavigatorHolder, executeCommands")
        commandsChannel.send(commands)
    }

    override suspend fun attachNavigator(navigator: CommandsExecutor) {
        Log.d("MIINE", "DefaultRouterNavigatorHolder, attachNavigator")
        commandsChannelJob?.cancel()
        currentExecutor = navigator
        commandsChannelJob = coroutineScope {
            launch(mainDispatcher + Job()) {
                commandsChannel
                    .receiveAsFlow()
                    .collect {
                        Log.d("MIINE", "DefaultRouterNavigatorHolder, collect")
                        currentExecutor?.executeCommands(*it)
                    }
            }
        }
    }

    override fun detachNavigator() {
        Log.d("MIINE", "DefaultRouterNavigatorHolder, detachNavigator")
        commandsChannelJob?.cancel()
        currentExecutor = null
    }
}
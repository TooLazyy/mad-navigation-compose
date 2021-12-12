package ru.wearemad.mad_compose_navigation.router.holder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.command.Command
import ru.wearemad.mad_compose_navigation.navigator.base.CommandsExecutor

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
        commandsChannel.send(commands)
    }

    override suspend fun attachNavigator(navigator: CommandsExecutor) {
        currentExecutor = navigator
        commandsChannelJob?.cancel()
        commandsChannelJob = coroutineScope {
            launch(mainDispatcher + Job()) {
                commandsChannel
                    .receiveAsFlow()
                    .collect {
                        currentExecutor?.executeCommands(*it)
                    }
            }
        }
    }

    override fun detachNavigator() {
        commandsChannelJob?.cancel()
        currentExecutor = null
    }
}
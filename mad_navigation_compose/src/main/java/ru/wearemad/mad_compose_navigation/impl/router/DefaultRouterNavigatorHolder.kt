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
import java.util.concurrent.atomic.AtomicReference

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
    private val currentExecutor: AtomicReference<CommandsExecutor?> = AtomicReference(null)

    init {
        Log.d("MIINE", "DefaultRouterNavigatorHolder init: $this")
    }

    override suspend fun executeCommands(vararg commands: Command) {
        val message = buildString {
            commands.forEach {
                append("command: ${it}\n")
            }
        }
        Log.d("MIINE", "DefaultRouterNavigatorHolder executeCommands: $message")
        commandsChannel.send(commands)
    }

    override suspend fun attachNavigator(navigator: CommandsExecutor) {
        Log.d("MIINE", "DefaultRouterNavigatorHolder attachNavigator: $navigator")
        if (currentExecutor.get() != null) {
            return
        }
        commandsChannelJob?.cancel()
        currentExecutor.set(navigator)
        commandsChannelJob = coroutineScope {
            launch(mainDispatcher + Job()) {
                Log.d("MIINE", "DefaultRouterNavigatorHolder attachNavigator collect")
                commandsChannel
                    .receiveAsFlow()
                    .collect {
                        Log.d("MIINE", "DefaultRouterNavigatorHolder attachNavigator, new commands: ${it.size}")
                        currentExecutor.get()?.executeCommands(*it)
                    }
            }
        }
    }

    override fun detachNavigator() {
        Log.d("MIINE", "DefaultRouterNavigatorHolder detachNavigator: $this")
        commandsChannelJob?.cancel()
        currentExecutor.set(null)
    }
}
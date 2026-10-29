package ru.wearemad.mad_compose_navigation.impl.navigator

import android.os.Bundle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.api.navigator.data.MainNavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.restorer.NavigatorRestorerParams

class AppMainNavigator(
    params: MainNavigatorParams,
) : BaseNavigator(params) {

    override val keepsLastScreen: Boolean = true

    private var restoreStateJob: Job? = null

    override fun restoreState(
        state: Bundle,
        factory: NavigatorFactory
    ) {
        restoreStateJob?.cancel()
        restoreStateJob = launch {
            val result = params.restorer.restore(
                NavigatorRestorerParams(
                    state,
                    params.instanceId,
                    factory,
                    params.inEventsChannel
                )
            )
            routesList = result.currentStack
            dialogRoutesList = result.currentDialogsStack
            setNavigators(result.nestedNavigators)
            onStackChanged()
        }
    }
}
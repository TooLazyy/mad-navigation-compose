package ru.wearemad.mad_compose_navigation.impl.navigator

import android.os.Bundle
import kotlinx.coroutines.launch
import ru.wearemad.mad_compose_navigation.api.navigator.data.NestedNavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.restorer.NavigatorRestorerParams
import ru.wearemad.mad_compose_navigation.impl.navigator.data.NestedStackChanged

class AppNestedNavigator(
    params: NestedNavigatorParams,
) : BaseNavigator(params) {

    override suspend fun afterStackChanged() {
        params.outEventsChannel?.send(NestedStackChanged)
    }

    override fun restoreState(
        state: Bundle,
        factory: NavigatorFactory
    ) {
        val result = params.restorer.restore(
            NavigatorRestorerParams(
                state,
                params.screenId,
                factory,
                params.inEventsChannel
            )
        )
        routesList = result.currentStack
        dialogRoutesList = result.currentDialogsStack
        setNavigators(result.nestedNavigators)
        launch {
            onStackChanged()
        }
    }
}
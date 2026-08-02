package ru.wearemad.mad_compose_navigation.api.navigator.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.api.route.Route
import java.util.UUID

@Parcelize
class StackEntry(
    val route: Route,
    val instanceId: String,
) : Parcelable {

    companion object {

        fun of(route: Route): StackEntry = StackEntry(
            route = route,
            instanceId = "${route.screenKey}_${UUID.randomUUID()}",
        )
    }

    val screenKey: String
        get() = route.screenKey
}
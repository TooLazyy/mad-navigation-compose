package ru.wearemad.mad_compose_navigation.api.restorer

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory

interface RestorableState {

    fun restoreState(
        state: Bundle,
        factory: NavigatorFactory
    )
}
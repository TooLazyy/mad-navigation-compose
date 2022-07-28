package ru.wearemad.mad_compose_navigation.impl.back_press

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import kotlinx.coroutines.channels.Channel
import ru.wearemad.mad_compose_navigation.api.back_press.BackPressCallbackHolder
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorEventsChannelEvent
import ru.wearemad.mad_compose_navigation.impl.navigator.data.DispatchSystemOnBackPressed

class DefaultBackPressCallbackHolder(
    canGoBack: Boolean,
    private val inEventsChannel: Channel<NavigatorEventsChannelEvent>
) : BackPressCallbackHolder {

    private val backPressedCallback = object : OnBackPressedCallback(canGoBack) {

        override fun handleOnBackPressed() {
            inEventsChannel.trySend(DispatchSystemOnBackPressed)
        }
    }

    override fun updateOnBackPressedCallback(canGoBack: Boolean) {
        backPressedCallback.isEnabled = canGoBack
    }

    override fun registerOnBackPressedCallback(dispatcher: OnBackPressedDispatcher?) {
        dispatcher?.addCallback(backPressedCallback)
    }

    override fun unregisterOnBackPressedCallback() {
        backPressedCallback.remove()
    }
}
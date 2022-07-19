package ru.wearemad.mad_compose_navigation.impl.back_press

import android.util.Log
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
            Log.d("MIINE", "handleOnBackPressed, send event")
            inEventsChannel.trySend(DispatchSystemOnBackPressed)
        }
    }

    override fun updateOnBackPressedCallback(canGoBack: Boolean) {
        backPressedCallback.isEnabled = canGoBack
    }

    override fun registerOnBackPressedCallback(dispatcher: OnBackPressedDispatcher?) {
        Log.d("MIINE", "registerOnBackPressedCallback")
        dispatcher?.addCallback(backPressedCallback)
    }

    override fun unregisterOnBackPressedCallback() {
        backPressedCallback.remove()
    }
}
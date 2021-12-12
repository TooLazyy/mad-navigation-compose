package ru.wearemad.mad_compose_navigation.navigator

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import ru.wearemad.mad_compose_navigation.navigator.base.BaseNavigator

open class AppNavigator(
    canGoBack: Boolean = false
) : BaseNavigator() {

    private val backPressedCallback = object : OnBackPressedCallback(canGoBack) {

        override fun handleOnBackPressed() {
            dispatchSystemOnBackPressed()
        }
    }

    override fun updateOnBackPressedCallback() {
        backPressedCallback.isEnabled = canGoBack
    }

    override fun registerOnBackPressedCallback(dispatcher: OnBackPressedDispatcher?) {
        dispatcher?.addCallback(backPressedCallback)
    }

    override fun unregisterOnBackPressedCallback() {
        backPressedCallback.remove()
    }
}
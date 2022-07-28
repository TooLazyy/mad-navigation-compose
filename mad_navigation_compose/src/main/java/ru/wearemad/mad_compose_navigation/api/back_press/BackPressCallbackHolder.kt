package ru.wearemad.mad_compose_navigation.api.back_press

import androidx.activity.OnBackPressedDispatcher

interface BackPressCallbackHolder {

    fun updateOnBackPressedCallback(canGoBack: Boolean)

    fun registerOnBackPressedCallback(dispatcher: OnBackPressedDispatcher?)

    fun unregisterOnBackPressedCallback()
}
package ru.wearemad.mad_compose_navigation.back_press.handler

import androidx.activity.OnBackPressedDispatcher

/**
 * Indicates main back press actions for Navigator
 */
interface BackPressHandler {

    val canGoBack: Boolean

    fun updateOnBackPressedCallback()

    fun registerOnBackPressedCallback(dispatcher: OnBackPressedDispatcher?)

    fun unregisterOnBackPressedCallback()

    fun dispatchSystemOnBackPressed()
}
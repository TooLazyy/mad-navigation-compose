package ru.wearemad.mad_compose_navigation.api.back_press

/**
 * Indicates main back press actions for Navigator
 */

interface BackPressHandler : BackPressCallbackHolder {

    val canGoBack: Boolean

    fun dispatchSystemOnBackPressed()
}
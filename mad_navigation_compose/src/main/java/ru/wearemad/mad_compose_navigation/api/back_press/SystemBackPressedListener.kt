package ru.wearemad.mad_compose_navigation.api.back_press

/**
 * Listens for system back press events
 */
fun interface SystemBackPressedListener {

    /**
     * Returns true if event has been handled, so it wont be passed further
     */
    fun onSystemBackHandled(): Boolean
}
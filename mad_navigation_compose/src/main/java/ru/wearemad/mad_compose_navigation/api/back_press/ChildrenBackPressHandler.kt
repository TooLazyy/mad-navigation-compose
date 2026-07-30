package ru.wearemad.mad_compose_navigation.api.back_press

import androidx.annotation.MainThread

interface ChildrenBackPressHandler {

    val hasSystemBackPressedListeners: Boolean

    @MainThread
    fun dispatchBackPressedToChildren(): Boolean

    @MainThread
    fun addSystemBackPressedListener(listener: SystemBackPressedListener)

    @MainThread
    fun removeSystemBackPressedListener(listener: SystemBackPressedListener)
}
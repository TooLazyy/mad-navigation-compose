package ru.wearemad.mad_compose_navigation.back_press.handler

import androidx.annotation.MainThread
import ru.wearemad.mad_compose_navigation.back_press.listener.SystemBackPressedListener

interface ChildrenBackPressHandler {

    @MainThread
    fun dispatchBackPressedToChildren(): Boolean

    @MainThread
    fun addSystemBackPressedListener(listener: SystemBackPressedListener)

    @MainThread
    fun removeSystemBackPressedListener(listener: SystemBackPressedListener)
}
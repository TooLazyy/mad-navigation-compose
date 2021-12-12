package ru.wearemad.mad_compose_navigation.back_press.handler

import ru.wearemad.mad_compose_navigation.back_press.listener.SystemBackPressedListener

class ChildrenBackPressHandlerDelegate : ChildrenBackPressHandler {

    private var systemBackPressedListeners = listOf<SystemBackPressedListener>()

    override fun dispatchBackPressedToChildren(): Boolean {
        if (systemBackPressedListeners.isEmpty()) {
            return false
        }
        for (i in systemBackPressedListeners.lastIndex downTo 0) {
            val backPressedConsumed = systemBackPressedListeners[i].onSystemBackHandled()
            if (backPressedConsumed) {
                return true
            }
        }
        return false
    }

    override fun addSystemBackPressedListener(listener: SystemBackPressedListener) {
        if (systemBackPressedListeners.contains(listener)) {
            return
        }
        systemBackPressedListeners = systemBackPressedListeners.toMutableList().apply {
            add(listener)
        }
    }

    override fun removeSystemBackPressedListener(listener: SystemBackPressedListener) {
        systemBackPressedListeners = systemBackPressedListeners.toMutableList().apply {
            remove(listener)
        }
    }
}
package ru.wearemad.mad_compose_navigation.api.saver

import android.os.Bundle

interface SavableState {

    fun saveState(): Bundle
}
package ru.wearemad.mad_compose_navigation.api.navigator

import kotlinx.coroutines.CoroutineScope

interface NavigatorScope : CoroutineScope {

    fun cancelJobs()
}
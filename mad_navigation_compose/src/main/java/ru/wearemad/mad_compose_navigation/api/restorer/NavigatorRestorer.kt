package ru.wearemad.mad_compose_navigation.api.restorer

interface NavigatorRestorer {

    fun restore(
        params: NavigatorRestorerParams
    ): RestoreStateResult
}

interface MainNavigatorRestorer : NavigatorRestorer

interface NestedNavigatorRestorer : NavigatorRestorer
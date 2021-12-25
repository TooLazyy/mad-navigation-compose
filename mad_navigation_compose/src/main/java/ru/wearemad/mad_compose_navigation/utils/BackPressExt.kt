package ru.wearemad.mad_compose_navigation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import ru.wearemad.mad_compose_navigation.back_press.listener.SystemBackPressedListener
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator

@Composable
fun Navigator.rememberBackPressHandler(backPressHandled: () -> Boolean) {
    val listener = remember(this) {
        SystemBackPressedListener {
            backPressHandled()
        }
    }
    LaunchedEffect(this) {
        addSystemBackPressedListener(listener)
    }
    DisposableEffect(this) {
        onDispose {
            this@rememberBackPressHandler.removeSystemBackPressedListener(listener)
        }
    }
}
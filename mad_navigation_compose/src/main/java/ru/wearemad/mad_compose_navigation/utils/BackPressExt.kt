package ru.wearemad.mad_compose_navigation.utils

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import ru.wearemad.mad_compose_navigation.api.back_press.SystemBackPressedListener
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator

private const val BackPressDebounceWindowMs = 500L

@Composable
fun Navigator.rememberBackPressHandler(backPressHandled: () -> Boolean) {
    val listener = remember(this) {
        var lastHandledAtMs: Long? = null
        SystemBackPressedListener {
            val nowMs = SystemClock.uptimeMillis()
            val lastMs = lastHandledAtMs
            if (lastMs != null && nowMs - lastMs < BackPressDebounceWindowMs) {
                return@SystemBackPressedListener true
            }
            val handled = backPressHandled()
            if (handled) {
                lastHandledAtMs = nowMs
            }
            handled
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
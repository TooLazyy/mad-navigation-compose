package ru.wearemad.mad_compose_navigation.route

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel

/**
 * Describes a particular screen
 * @param key - should be unique. if some route could be present in multiple instances - use random uid
 * @param args - optional arguments can be passed to a route
 */
abstract class Route(
    open val key: String? = null,
    val args: Bundle? = null
) : Parcelable {

    /**
     * Composable content for that route
     */
    @IgnoredOnParcel
    abstract val content: @Composable (id: String, args: Bundle?) -> Unit

    val screenKey: String
        get() = key ?: this::class.java.name
}
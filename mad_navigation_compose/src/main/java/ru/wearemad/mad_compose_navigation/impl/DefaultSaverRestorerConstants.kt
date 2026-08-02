package ru.wearemad.mad_compose_navigation.impl

//region main
internal const val KEY_COMPOSE_MAIN_NAVIGATOR_BUNDLE = "key_compose_main_navigator_bundle"
internal const val KEY_COMPOSE_MAIN_NAVIGATOR_ROUTES_DATA = "key_compose_main_navigator_routes_data"
internal const val KEY_COMPOSE_MAIN_NAVIGATOR_DIALOGS_DATA = "key_compose_main_navigator_dialogs_data"
internal const val KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATORS_DATA =
    "key_compose_main_navigator_nested_navigators_data"
internal const val KEY_COMPOSE_MAIN_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX =
    "key_compose_main_navigator_nested_navigator_data_prefix_"
//endregion

//region nested
internal const val KEY_COMPOSE_NESTED_NAVIGATOR_BUNDLE_PREFIX_ = "key_compose_nested_navigator_bundle_prefix_"
internal const val KEY_COMPOSE_NESTED_NAVIGATOR_ROUTES_DATA_PREFIX =
    "key_compose_nested_navigator_routes_data_prefix_"
internal const val KEY_COMPOSE_NESTED_NAVIGATOR_DIALOGS_DATA_PREFIX =
    "key_compose_nested_navigator_dialogs_data_prefix_"
internal const val KEY_COMPOSE_NESTED_NAVIGATOR_NESTED_DATA_PREFIX =
    "key_compose_nested_navigator_nested_data_prefix_"
internal const val KEY_COMPOSE_NESTED_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX =
    "key_compose_nested_navigator_nested_navigator_data_prefix_"
//endregion

internal fun buildNestedKey(instanceId: String): String =
    KEY_COMPOSE_NESTED_NAVIGATOR_NESTED_DATA_PREFIX + instanceId

internal fun buildBundlePrefix(instanceId: String): String =
    KEY_COMPOSE_NESTED_NAVIGATOR_BUNDLE_PREFIX_ + instanceId

internal fun buildRoutesKey(instanceId: String): String =
    KEY_COMPOSE_NESTED_NAVIGATOR_ROUTES_DATA_PREFIX + instanceId

internal fun buildDialogsKey(instanceId: String): String =
    KEY_COMPOSE_NESTED_NAVIGATOR_DIALOGS_DATA_PREFIX + instanceId

internal fun buildNestedNavigatorDataKey(instanceId: String): String =
    KEY_COMPOSE_NESTED_NAVIGATOR_NESTED_NAVIGATOR_DATA_PREFIX + instanceId
package ru.wearemad.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.impl.command.Add
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.MainNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactoryParams
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.impl.navigator.navigator_factory.DefaultNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.impl.command.Back
import ru.wearemad.mad_compose_navigation.utils.defaultMainNavigatorFactory
import ru.wearemad.mad_compose_navigation.utils.defaultNestedNavigatorFactory
import kotlin.coroutines.CoroutineContext

class MainActivity :
    AppCompatActivity(),
    CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //factories
        val mainFactory: MainNavigatorFactory = defaultMainNavigatorFactory()
        val nestedFactory: NestedNavigatorFactory = defaultNestedNavigatorFactory()
        val factory = DefaultNavigatorFactory(mainFactory, nestedFactory)

        //main
        val mainNavigator = rememberNavigator("root", factory)
        mainNavigator.registerOnBackPressedCallback(onBackPressedDispatcher)
        launch {
            mainNavigator
                .stateFlow
                .collect {
                    var a = it.hashCode() + 1
                    a += 1
                }
        }
        launch {
            delay(1_000L)
            mainNavigator.executeCommands(
                Add(Route1("screen_1"))
            )
            delay(1_000L)
            mainNavigator.executeCommands(
                Add(Route1("screen_2"))
            )
            /*delay(5_000)
            val screen1NestedNav = mainNavigator.getOrCreateNestedNavigator(
                "screen_1",
                nestedFactory
            )
            screen1NestedNav.executeCommands(Add(Route1("screen_1_1")))
            delay(5_000)
            screen1NestedNav.executeCommands(Add(Route1("screen_1_2")))
            val screen1_1NestedNav = screen1NestedNav.getOrCreateNestedNavigator(
                "screen_1_1",
                nestedFactory
            )
            screen1_1NestedNav.executeCommands(Add(Route1("screen_1_1_1")))
            delay(15_000)*/
        }
/*        val nested_1 = mainNavigator.getOrCreateNestedNavigator(
            "screen_1",
            nestedFactory
        )
        //level 1
        val nested_2 = mainNavigator.getOrCreateNestedNavigator(
            "screen_2",
            nestedFactory
        )
        val nested_1_1 = nested_1.getOrCreateNestedNavigator(
            "screen_1_1",
            nestedFactory
        )*/
    }

    private fun rememberNavigator(
        screenId: String,
        factory: NavigatorFactory,
    ): Navigator = factory.create(
        NavigatorFactoryParams.MainNavigatorParams(screenId)
    )

    @Parcelize
    class Route1(
        override val key: String
    ) : Route(key) {

        @IgnoredOnParcel
        override val content: (id: String, args: Bundle?) -> Unit = { _, _ -> }
    }
}
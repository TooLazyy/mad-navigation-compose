package ru.wearemad.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.api.navigator.Navigator
import ru.wearemad.mad_compose_navigation.api.navigator.data.NavigatorState
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactoryParams
import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.impl.command.Add
import ru.wearemad.mad_compose_navigation.impl.command.NewRoot
import ru.wearemad.mad_compose_navigation.impl.navigator.navigator_factory.DefaultNavigatorFactory
import ru.wearemad.mad_compose_navigation.utils.defaultMainNavigatorFactory
import ru.wearemad.mad_compose_navigation.utils.defaultNestedNavigatorFactory
import kotlin.coroutines.CoroutineContext

class MainActivity :
    AppCompatActivity(),
    CoroutineScope {

    private var someJob: Job? = null

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainNavigatorFactory = defaultMainNavigatorFactory()
        val nestedNavigatorFactory = defaultNestedNavigatorFactory()

        val factory = DefaultNavigatorFactory(mainNavigatorFactory, nestedNavigatorFactory)

        val mainNav = factory.create(
            NavigatorFactoryParams.MainNavigatorParams("root")
        )
        doItAgain(mainNav)
        launch {
            //before save
            delay(2_000L)
            mainNav.executeCommands(NewRoot(TestRoute("Screen Splash")))
            delay(2_000L)
            mainNav.executeCommands(Add(TestRoute("Screen A")))
            delay(2_000L)
            mainNav.executeCommands(Add(TestRoute("Screen B")))
            delay(2_000L)
            val bNestedRouter = mainNav.getOrCreateNestedNavigator("Screen B", nestedNavigatorFactory)
            bNestedRouter.executeCommands(
                NewRoot(TestRoute("Screen Sub B"))
            )
            delay(2_000L)

            //save
            val savedState = mainNav.saveState()

            Log.d("MIINE", "state saved")
            val newMainNavigator = factory.create(
                NavigatorFactoryParams.MainNavigatorParams("root")
            )
            doItAgain(newMainNavigator)
            delay(2_000L)
            Log.d("MIINE", "restore state")
            newMainNavigator.restoreState(savedState, factory)
        }
    }

    private fun doItAgain(mainNav: Navigator) {
        someJob?.cancel()
        someJob = null
        Log.d("MIINE", "subscribe to state")
        someJob = launch {
            mainNav
                .stateFlow
                .drop(1)
                .map {
                    Log.d("MIINE", "before flattenNavigatorBackStack")
                    it
                }
                .map(::flattenNavigatorBackStack)
                .collect {

                }
        }
    }

    private fun flattenNavigatorBackStack(rootState: NavigatorState): List<String> {
        Log.d("MIINE", "flattenNavigatorBackStack: $rootState\n")
        val selfScreensIds = rootState
            .currentStack
            .map { it.screenKey } +
                rootState
                    .currentDialogsStack
                    .map { it.screenKey }
        return selfScreensIds + rootState.nestedNavigatorsState
            .map { flattenNavigatorBackStack(it) }
            .flatten()
    }

    @Parcelize
    class TestRoute(
        override val key: String?
    ) : Route(key = key) {

        @IgnoredOnParcel
        override val content: (id: String, args: Bundle?) -> Unit = { _, _ ->}

        override fun toString(): String = "Route: $key"
    }
}
package com.badoo.ribs.sandbox.rib.switcher

import com.badoo.ribs.android.activitystarter.CanProvideActivityStarter
import com.badoo.ribs.android.dialog.CanProvideDialogLauncher
import com.badoo.ribs.android.permissionrequester.CanProvidePermissionRequester
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.portal.CanProvidePortal
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExample
import com.badoo.ribs.sandbox.rib.foo_bar.FooBar
import com.badoo.ribs.sandbox.rib.hello_world.HelloWorld
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherRouter.Configuration
import com.badoo.ribs.sandbox.rib.switcher.routing.SwitcherTransitionHandler
import com.badoo.ribs.sandbox.util.CoffeeMachine
import io.reactivex.rxjava3.core.Single

interface Switcher : Rib {

    interface Dependency :
        CanProvideActivityStarter,
        CanProvidePermissionRequester,
        CanProvideDialogLauncher,
        CanProvidePortal {

        val coffeeMachine: CoffeeMachine
    }

    sealed class Input

    sealed class Output

    class Customisation(
        val viewFactory: SwitcherView.Factory = SwitcherViewImpl.Factory(),
        val transitionHandler: TransitionHandler<Configuration> = SwitcherTransitionHandler(duration = 2000)
    ) : RibCustomisation

    // Workflow
    fun attachHelloWorld(): Single<HelloWorld>
    fun attachDialogExample(): Single<DialogExample>
    fun attachFooBar(): Single<FooBar>
    fun waitForHelloWorld(): Single<HelloWorld>
    fun doSomethingAndStayOnThisNode(): Single<Switcher>
}

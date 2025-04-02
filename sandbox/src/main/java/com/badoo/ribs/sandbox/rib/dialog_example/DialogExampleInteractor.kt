package com.badoo.ribs.sandbox.rib.dialog_example

import androidx.lifecycle.Lifecycle
import com.badoo.mvicore.android.lifecycle.createDestroy
import com.badoo.mvicore.android.lifecycle.startStop
import com.badoo.ribs.android.dialog.Dialog
import com.badoo.ribs.android.dialog.Dialog.CancellationPolicy.Cancellable
import com.badoo.ribs.android.text.Text
import com.badoo.ribs.clienthelper.childaware.whenChildBuilt
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.source.backstack.BackStack
import com.badoo.ribs.routing.source.backstack.operation.pushOverlay
import com.badoo.ribs.rx3.adapter.rx3
import com.badoo.ribs.sandbox.R
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowLazyDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowRibDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowSimpleDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.ShowThemedDialogClicked
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.Event.Up
import com.badoo.ribs.sandbox.rib.dialog_example.DialogExampleView.ViewModel
import com.badoo.ribs.sandbox.rib.dialog_example.dialog.Dialogs
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration
import com.badoo.ribs.sandbox.rib.dialog_example.routing.DialogExampleRouter.Configuration.Overlay
import com.badoo.ribs.sandbox.rib.lorem_ipsum.LoremIpsum
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.functions.Consumer

class DialogExampleInteractor internal constructor(
    buildParams: BuildParams<Nothing?>,
    private val backStack: BackStack<Configuration>,
    private val dialogs: Dialogs
) : Interactor<DialogExample, DialogExampleView>(
    buildParams = buildParams
) {

    private val dummyViewInput = BehaviorRelay.createDefault(
        ViewModel("Dialog examples")
    )

    override fun onCreate(nodeLifecycle: Lifecycle) {
        whenChildBuilt<LoremIpsum>(nodeLifecycle) { commonLifecycle, child ->
            commonLifecycle.createDestroy {
                bind(child.output to loremIpsumOutputConsumer)
            }
        }
    }

    override fun onViewCreated(view: DialogExampleView, viewLifecycle: Lifecycle) {
        viewLifecycle.startStop {
            bind(dummyViewInput to view)
            bind(view to viewEventConsumer)
            bind(dialogs.themedDialog.rx3() to dialogEventConsumer)
            bind(dialogs.simpleDialog.rx3() to dialogEventConsumer)
            bind(dialogs.lazyDialog.rx3() to dialogEventConsumer)
            bind(dialogs.ribDialog.rx3() to dialogEventConsumer)
        }
    }

    private val viewEventConsumer: Consumer<DialogExampleView.Event> = Consumer {
        when (it) {
            ShowThemedDialogClicked -> backStack.pushOverlay(Overlay.ThemedDialog)
            ShowSimpleDialogClicked -> backStack.pushOverlay(Overlay.SimpleDialog)
            ShowLazyDialogClicked -> {
                initLazyDialog()
                backStack.pushOverlay(Overlay.LazyDialog)
            }

            ShowRibDialogClicked -> backStack.pushOverlay(Overlay.RibDialog)
            Up -> node.upNavigation()
        }
    }

    private fun initLazyDialog() {
        with(dialogs.lazyDialog) {
            title = Text.Resource(R.string.lazy_dialog_title)
            message = Text.Plain("Lazy dialog message")
            buttons {
                neutral(Text.Plain("Lazy neutral button"), Dialog.Event.Neutral)
            }
            cancellationPolicy = Cancellable(
                event = Dialog.Event.Cancelled,
                cancelOnTouchOutside = true
            )
        }
    }

    private val dialogEventConsumer: Consumer<Dialog.Event> = Consumer {
        when (it) {
            Dialog.Event.Positive -> {
                dummyViewInput.accept(ViewModel("Dialog - Positive clicked"))
            }

            Dialog.Event.Negative -> {
                dummyViewInput.accept(ViewModel("Dialog - Negative clicked"))
            }

            Dialog.Event.Neutral -> {
                dummyViewInput.accept(ViewModel("Dialog - Neutral clicked"))
            }

            Dialog.Event.Cancelled -> {
                dummyViewInput.accept(ViewModel("Dialog - Cancelled"))
            }
        }
    }

    private val loremIpsumOutputConsumer: Consumer<LoremIpsum.Output> = Consumer {
        dummyViewInput.accept(ViewModel("Button in Lorem Ipsum RIB clicked"))
        backStack.popBackStack()
    }
}

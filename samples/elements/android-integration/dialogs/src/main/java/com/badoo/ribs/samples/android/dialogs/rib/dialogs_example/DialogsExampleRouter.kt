package com.badoo.ribs.samples.android.dialogs.rib.dialogs_example

import android.os.Parcelable
import com.badoo.ribs.android.dialog.DialogLauncher
import com.badoo.ribs.android.dialog.routing.resolution.DialogResolution.Companion.showDialog
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.samples.android.dialogs.dialogs.Dialogs
import com.badoo.ribs.samples.android.dialogs.rib.dialogs_example.DialogsExampleRouter.Configuration
import com.badoo.ribs.samples.android.dialogs.rib.dialogs_example.DialogsExampleRouter.Configuration.Content
import com.badoo.ribs.samples.android.dialogs.rib.dialogs_example.DialogsExampleRouter.Configuration.Overlay
import kotlinx.parcelize.Parcelize

class DialogsExampleRouter(
    buildParams: BuildParams<Nothing?>,
    routingSource: RoutingSource<Configuration>,
    private val dialogLauncher: DialogLauncher,
    private val dialogs: Dialogs
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource
) {

    sealed class Configuration : Parcelable {
        sealed class Content : Configuration() {
            @Parcelize
            data object Default : Content()
        }

        sealed class Overlay : Configuration() {
            @Parcelize
            data object ThemedDialog : Overlay()

            @Parcelize
            data object SimpleDialog : Overlay()

            @Parcelize
            data object LazyDialog : Overlay()

            @Parcelize
            data object RibDialog : Overlay()
        }
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        when (routing.configuration) {
            is Content.Default -> Resolution.noop()
            is Overlay.ThemedDialog -> showDialog(
                routingSource,
                routing.identifier,
                dialogLauncher,
                dialogs.themedDialog
            )

            is Overlay.SimpleDialog -> showDialog(
                routingSource,
                routing.identifier,
                dialogLauncher,
                dialogs.simpleDialog
            )

            is Overlay.LazyDialog -> showDialog(
                routingSource,
                routing.identifier,
                dialogLauncher,
                dialogs.lazyDialog
            )

            is Overlay.RibDialog -> showDialog(
                routingSource,
                routing.identifier,
                dialogLauncher,
                dialogs.ribDialog
            )
        }
}

package com.badoo.ribs.samples.gallery.rib.android.container.routing

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.resolution.ChildResolution.Companion.child
import com.badoo.ribs.routing.resolution.Resolution
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.badoo.ribs.samples.gallery.rib.android.container.routing.AndroidContainerRouter.Configuration
import kotlinx.parcelize.Parcelize

class AndroidContainerRouter internal constructor(
    buildParams: BuildParams<*>,
    routingSource: RoutingSource<Configuration>,
    private val builders: AndroidContainerChildBuilders,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = routingSource,
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        @Parcelize
        data object Picker : Configuration()
        @Parcelize
        data object AcitivityExample : Configuration()
        @Parcelize
        data object PermissionsExample : Configuration()
        @Parcelize
        data object DialogExample : Configuration()
    }

    override fun resolve(routing: Routing<Configuration>): Resolution =
        with(builders) {
            when (routing.configuration) {
                Configuration.Picker -> child { picker.build(it) }
                Configuration.AcitivityExample -> child { activities.build(it) }
                Configuration.PermissionsExample -> child { permissions.build(it) }
                Configuration.DialogExample -> child { dialogs.build(it) }
            }
        }
}


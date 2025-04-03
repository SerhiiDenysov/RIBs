package com.badoo.ribs.samples.gallery.rib.other.container

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.other.container.OtherContainer.Input
import com.badoo.ribs.samples.gallery.rib.other.container.OtherContainer.Output

class OtherContainerNode internal constructor(
    buildParams: BuildParams<*>,
    plugins: List<Plugin> = emptyList(),
    viewFactory: ViewFactory<OtherContainerView>,
    connector: NodeConnector<Input, Output> = NodeConnector(),
) : RxWorkflowNode<OtherContainerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), OtherContainer, Connectable<Input, Output> by connector

package com.badoo.ribs.samples.gallery.rib.android.picker

import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.core.plugin.Plugin
import com.badoo.ribs.core.view.ViewFactory
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.rx3.clienthelper.connector.NodeConnector
import com.badoo.ribs.rx3.workflows.RxWorkflowNode
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker.Input
import com.badoo.ribs.samples.gallery.rib.android.picker.AndroidPicker.Output

class AndroidPickerNode internal constructor(
    buildParams: BuildParams<*>,
    viewFactory: ViewFactory<AndroidPickerView>?,
    plugins: List<Plugin> = emptyList(),
    connector: NodeConnector<Input, Output> = NodeConnector()
) : RxWorkflowNode<AndroidPickerView>(
    buildParams = buildParams,
    viewFactory = viewFactory,
    plugins = plugins
), AndroidPicker, Connectable<Input, Output> by connector

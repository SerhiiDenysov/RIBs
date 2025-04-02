package com.badoo.ribs.samples.gallery.rib.other.picker

import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.customisation.RibCustomisation
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPicker.Input
import com.badoo.ribs.samples.gallery.rib.other.picker.OtherPicker.Output

interface OtherPicker : Rib, Connectable<Input, Output> {

    interface Dependency

    sealed class Input

    sealed class Output {
        data object RetainedInstanceStoreSelected : Output()
    }

    class Customisation(
        val viewFactory: OtherPickerView.Factory = OtherPickerViewImpl.Factory()
    ) : RibCustomisation
}

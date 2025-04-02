package com.badoo.common.ribs.rx3

import android.view.ViewGroup
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.Rib
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.rx3.clienthelper.connector.Connectable
import com.badoo.ribs.test.InteractorTestHelper
import com.jakewharton.rxrelay3.PublishRelay
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Observer
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Deprecated("Use RibInteractorTestHelper")
inline fun <reified View, ViewEvent : Any> createInteractorTestHelper(
    interactor: Interactor<*, View>,
    viewEventRelay: Relay<ViewEvent>
): InteractorTestHelper<View> where View : RibView, View : ObservableSource<ViewEvent> {
    val view: View = viewEventRelay.subscribedView()
    return InteractorTestHelper(interactor) { view }
}

@Deprecated("Use RibInteractorTestHelper")
inline fun <reified R, reified Input, reified Output> Interactor<R, *>.mockIO(
    inputRelay: Relay<Input> = PublishRelay.create(),
    outputRelay: Relay<Output> = PublishRelay.create()
) where R : Rib, R : Connectable<Input, Output> {
    val rib = mock<R>().apply {
        whenever(input).thenReturn(inputRelay)
        whenever(output).thenReturn(outputRelay)
    }
    init(rib)
}

@Deprecated("Use RibInteractorTestHelper")
inline fun <reified RView, ViewEvent : Any> Relay<ViewEvent>.subscribedView(): RView where RView : RibView, RView : ObservableSource<ViewEvent> =
    mock<RView>().apply {
        whenever(this.androidView).thenReturn(mock<ViewGroup>())
        whenever(this.subscribe(any())).thenAnswer {
            val observer = it.getArgument<Observer<ViewEvent>>(0)
            this@subscribedView.subscribe(observer)
        }
    }

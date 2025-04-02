package com.badoo.ribs.rx3.workflows

import android.content.Intent
import android.os.Bundle
import com.badoo.ribs.android.RibActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class RxWorkflowActivity : RibActivity() {

    private val disposables = CompositeDisposable()

    open val workflowFactory: (Intent) -> Observable<*>? = {
        null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == Intent.ACTION_VIEW) {
            handleDeepLink(intent)
        }
    }

    fun handleDeepLink(intent: Intent) {
        workflowFactory.invoke(intent)?.let {
            disposables.add(it.subscribe())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}

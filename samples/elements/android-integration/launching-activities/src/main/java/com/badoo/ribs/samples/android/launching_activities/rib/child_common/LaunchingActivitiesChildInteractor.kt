package com.badoo.ribs.samples.android.launching_activities.rib.child_common

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Lifecycle
import com.badoo.ribs.android.activitystarter.ActivityStarter
import com.badoo.ribs.android.subscribe
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.minimal.reactive.CompositeCancellable
import com.badoo.ribs.minimal.reactive.Relay
import com.badoo.ribs.samples.android.launching_activities.app.OtherActivity

class LaunchingActivitiesChildInteractor(
    buildParams: BuildParams<Nothing?>,
    private val activityStarter: ActivityStarter
) : Interactor<LaunchingActivitiesChildBase, LaunchingActivitiesChildView>(
    buildParams = buildParams
) {
    private val cancellable = CompositeCancellable()
    private val dataReturnedRelay = Relay<String>()

    override fun onViewCreated(view: LaunchingActivitiesChildView, viewLifecycle: Lifecycle) {

        viewLifecycle.subscribe(
            onCreate = {
                cancellable += activityStarter
                    .events(this@LaunchingActivitiesChildInteractor)
                    .observe(::onActivityEvent)
                cancellable += view.events.observe(::onViewEvent)
                cancellable += dataReturnedRelay.observe { view.setData(it) }
            },
            onDestroy = {
                cancellable.cancel()
            }
        )
    }

    private fun onViewEvent(event: LaunchingActivitiesChildView.Event) {
        when (event) {
            is LaunchingActivitiesChildView.Event.LaunchActivityForResult ->
                activityStarter.startActivityForResult(this, REQUEST_CODE_OTHER_ACTIVITY) {
                    Intent(this, OtherActivity::class.java)
                        .putExtra(OtherActivity.KEY_INCOMING, event.data)
                }
        }
    }

    private fun onActivityEvent(activityEvent: ActivityStarter.ActivityResultEvent) {
        if (activityEvent.requestCode == REQUEST_CODE_OTHER_ACTIVITY) {
            if (activityEvent.resultCode == Activity.RESULT_OK) {
                dataReturnedRelay.accept(
                    activityEvent.data?.getStringExtra(OtherActivity.KEY_OUTGOING)
                        ?: ""
                )
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_OTHER_ACTIVITY = 1
    }

}

package com.badoo.ribs.sandbox.rib.portal_overlay_test

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.sandbox.rib.portal_overlay_test.PortalOverlayTest.Customisation

class PortalOverlayTestBuilder(
    private val dependency: PortalOverlayTest.Dependency
) : SimpleBuilder<PortalOverlayTest>() {

    override fun build(buildParams: BuildParams<Nothing?>): PortalOverlayTest {
        val customisation = buildParams.getOrDefault(Customisation())
        val interactor = PortalOverlayTestInteractor(buildParams)

        return PortalOverlayTestNode(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(
                interactor
            )
        )
    }
}

package com.badoo.ribs.example.rib.small.builder

import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.small.Small
import com.badoo.ribs.example.rib.small.SmallNode

class SmallBuilder(
    dependency: Small.Dependency
) : Builder<Small.Dependency, SmallNode>() {

    override val dependency : Small.Dependency = object : Small.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Small::class)
    }

    override val rib: Rib =
        object : Small {}

    override fun build(buildParams: BuildParams<Nothing?>): SmallNode =
        DaggerSmallComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Small.Customisation()),
                buildParams = buildParams
            )
            .node()
}

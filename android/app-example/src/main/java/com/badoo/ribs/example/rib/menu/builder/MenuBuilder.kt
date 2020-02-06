package com.badoo.ribs.example.rib.menu.builder

import com.badoo.ribs.core.builder.BuildParams
import com.badoo.ribs.core.builder.Builder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.Rib
import com.badoo.ribs.customisation.customisationsBranchFor
import com.badoo.ribs.customisation.getOrDefault
import com.badoo.ribs.example.rib.menu.Menu
import com.badoo.ribs.example.rib.menu.MenuView

class MenuBuilder(
    dependency: Menu.Dependency
) : Builder<Menu.Dependency, Node<MenuView>>() {

    override val dependency : Menu.Dependency = object : Menu.Dependency by dependency {
        override fun ribCustomisation() = dependency.customisationsBranchFor(Menu::class)
    }

    override val rib: Rib =
        object : Menu {}

    override fun build(buildParams: BuildParams<Nothing?>): Node<MenuView> =
        DaggerMenuComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = dependency.getOrDefault(Menu.Customisation()),
                buildParams = buildParams
            )
            .node()
}

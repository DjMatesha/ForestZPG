package forestZPG.view

import tornadofx.*

class MainView : View("forest") {
    override val root = borderpane {
        left<LeftView>()
        center<CenterView>()
        right<Timer>()
    }

    init {
        with(root) {
            prefWidth = 1100.0
            prefHeight = 600.0
        }
    }
}

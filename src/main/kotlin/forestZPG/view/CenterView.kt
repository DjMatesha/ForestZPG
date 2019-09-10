package forestZPG.view

import forestZPG.forest.Forest
import tornadofx.*

import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle

class CenterView : View() {
    override val root = Pane()

    init {
        with(root) {
            style { borderColor += box(c("#a1a1a1")) }
        }
        subscribe<IterRequest> {
            Forest.makeIteration()
            if (Forest.isDead()){
                fire(StartRequest(false))
                fire(DeadForest())
                root.clear()
            }
        }
        subscribe<CreateForestRequest> { event ->
            root.add(Rectangle(root.width,root.height, c("#a1a1a1")))
            Forest.initForest(event.height, event.width, event.herbivores, event.predators, event.foodProb)
        }
    }
}
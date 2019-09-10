package forestZPG.forest

import forestZPG.forest.Interfaces.Сonsumption.HerbivorousEating
import forestZPG.forest.Interfaces.Сonsumption.PredatorEating
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.image.ImageView
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import tornadofx.*
import forestZPG.view.CenterView
import forestZPG.view.Timer
import java.io.File


object DrawPart {
    val canvas = find(CenterView::class).root
    private var width = canvas.width / Forest.width
    private var height = canvas.height / Forest.height
    private const val border = 1
    private const val animalSize = 25.0
    fun drawUpdate() {
        width = canvas.width / Forest.width
        height = canvas.height / Forest.height
    }

    fun drawTree(tree: Tree) {
        val x = tree.coordinates.second * width
        val y = tree.coordinates.first * height
        val img = ImageView(File("src\\main\\images\\$tree.png").toURI().toString())
        img.relocate(x + border, y + border)
        img.fitHeight = height - 2 * border
        img.fitWidth = width - 2 * border
        canvas.add(img)

        img.setOnMouseClicked {
            if (!Timer.work) {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Сведения о дереве"
                alert.headerText = null
                alert.contentText = tree.createReport()
                alert.showAndWait()
            }
        }
    }

    private fun getPosition(tree: Tree): Pair<Double, Double> = Pair(tree.coordinates.second * width, tree.coordinates.first * height)

    fun getPosition(animal: Animal, location: TreePart): Pair<Double, Double> {
        val (x, y) = getPosition(location.tree)
        when (location) {
            is Roots -> {
                val stepX = (width - border - animalSize) / 5
                val begX = x + border
                val begY = y + height - border - animalSize
                return when (animal) {
                    is Squirrel -> Pair(begX, begY)
                    is FlyingSquirrel -> Pair(begX + stepX, begY)
                    is Woodpecker -> Pair(begX + 2 * stepX, begY)
                    is Chipmunk -> Pair(begX + 3 * stepX, begY)
                    is Badger -> Pair(begX + 4 * stepX, begY)
                    is Wolf -> Pair(begX + 5 * stepX, begY)
                    else -> throw IllegalArgumentException("Wrong animal type")
                }
            }
            is Trunk -> {
                val stepY = (height - border - animalSize) / 5
                val begX = x + (width - animalSize) / 2
                val begY = y + border
                return when (animal) {
                    is Squirrel -> Pair(begX, begY)
                    is FlyingSquirrel -> Pair(begX, begY + stepY)
                    is Woodpecker -> Pair(begX, begY + 2 * stepY)
                    is Chipmunk -> Pair(begX, begY + 3 * stepY)
                    else -> throw IllegalArgumentException("Wrong animal type")
                }
            }
            is Crown -> {
                val stepX = width / 6
                val stepY = height / 8
                val begX = x + (width - animalSize) / 2
                val begY = y + height / 6
                return when (animal) {
                    is Squirrel -> Pair(begX + stepX, begY)
                    is FlyingSquirrel -> Pair(begX - stepX, begY)
                    is Woodpecker -> Pair(begX + 2 * stepX, begY + stepY)
                    is Chipmunk -> Pair(begX - 2 * stepX, begY + stepY)
                    is Vulture -> Pair(begX - stepX, begY + 3 * stepY)
                    else -> throw Exception("Wrong animal type")
                }
            }
        }
    }

    fun getPosition(animal: Animal): Pair<Double, Double> = getPosition(animal, animal.location)

    fun getColor(animal: Animal): Color = when (animal) {
        is Squirrel -> Color.ORANGERED
        is FlyingSquirrel -> Color.LIGHTGRAY
        is Woodpecker -> Color.CADETBLUE
        is Chipmunk -> Color.SANDYBROWN
        is Badger -> Color.SADDLEBROWN
        is Vulture -> Color.ANTIQUEWHITE
        is Wolf -> Color.GRAY
    }

    fun getSize(animal: Animal) = when (animal) {
        is HerbivorousEating -> 10.0
        is PredatorEating -> 12.5
        else -> throw Exception("Wrong animal type")
    }
}

class GameObject(animal: Animal) {
    private val canvas = DrawPart.canvas
    private val img = Circle(DrawPart.getSize(animal), DrawPart.getColor(animal))
    private val text = Text(animal.animalCount.toString())

    init {
        val p = DrawPart.getPosition(animal)
        img.relocate(p.first, p.second)
        img.setOnMouseClicked {
            if (!Timer.work) {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Сведения о стае"
                alert.headerText = null
                alert.contentText = animal.toString()
                alert.showAndWait()
            }
        }
        text.relocate(p.first + 5, p.second + 5)
        text.font = Font.font("TimesRoman", FontWeight.BOLD, 12.0)
        text.setOnMouseClicked {
            if (!Timer.work) {
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Сведения о стае"
                alert.headerText = null
                alert.contentText = animal.toString()
                alert.showAndWait()
            }
        }
        canvas.children.addAll(img, text)
    }

    fun setText(value: String) {
        text.text = value
    }

    fun remove() {
        canvas.children.remove(img)
        canvas.children.remove(text)
    }

    fun drawMove(animal: Animal, nextPos: TreePart) {
        val begin: Pair<Double, Double> = DrawPart.getPosition(animal)
        val end: Pair<Double, Double> = DrawPart.getPosition(animal, nextPos)
        val timeline = javafx.animation.Timeline(javafx.animation.KeyFrame(javafx.util.Duration.millis(20.0),
                object : EventHandler<ActionEvent> {
                    var dx = (end.first - begin.first) / 20 //Step on x or velocity
                    var dy = (end.second - begin.second) / 20 //Step on y

                    override fun handle(t: ActionEvent) {
                        //move the ball
                        img.layoutX = img.layoutX + dx
                        img.layoutY = img.layoutY + dy
                        text.layoutX = text.layoutX + dx
                        text.layoutY = text.layoutY + dy
                    }
                }))
        timeline.cycleCount = 20
        timeline.play()
    }
}



package forestZPG.view

import javafx.scene.paint.Color
import tornadofx.*
import java.util.regex.Pattern
import javafx.geometry.Pos
import javafx.scene.control.*
import java.io.File
import kotlin.math.max
import kotlin.math.min


class LeftView : View() {
    private var forestWidth: TextField by singleAssign()
    private var forestHeight: TextField by singleAssign()
    private var foodProb: Slider by singleAssign()
    private var infoLabel: Label by singleAssign()
    private var herbivoreMin: TextField by singleAssign()
    private var herbivoreMax: TextField by singleAssign()
    private var predatorMin: TextField by singleAssign()
    private var predatorMax: TextField by singleAssign()
    private var create: Button by singleAssign()
    private var start: ToggleButton by singleAssign()
    override val root = vbox {
        form {
            minHeight = 400.0
            fieldset("Лес") {
                field("Ширина (1-10)") {
                    forestWidth = textfield("5")
                    val p = Pattern.compile("""|[1-9]|10""")
                    forestWidth.textProperty().addListener { _, oldValue, newValue -> if (!p.matcher(newValue).matches()) forestWidth.text = oldValue }
                    forestWidth.focusedProperty().addListener { _, _, isNowFocused ->
                        if (!isNowFocused)
                            if (forestWidth.text == "") forestWidth.text = "1"
                    }

                }
                field("Высота (1-10)") {
                    forestHeight = textfield("5")
                    val p = Pattern.compile("""|[1-9]|10""")
                    forestHeight.textProperty().addListener { _, oldValue, newValue -> if (!p.matcher(newValue).matches()) forestHeight.text = oldValue }
                    forestHeight.focusedProperty().addListener { _, _, isNowFocused ->
                        if (!isNowFocused)
                            if (forestHeight.text == "") forestHeight.text = "1"
                    }
                }
            }
            fieldset("Система") {
                field("Число травоядных (1-99)") {
                    hbox {
                        herbivoreMin = textfield("10")
                        val p = Pattern.compile("""|[1-9][\d]|[1-9]""")
                        herbivoreMin.textProperty().addListener { _, oldValue, newValue -> if (!p.matcher(newValue).matches()) herbivoreMin.text = oldValue }
                        herbivoreMin.focusedProperty().addListener { _, _, isNowFocused ->
                            if (!isNowFocused)
                                if (herbivoreMin.text == "") herbivoreMin.text = "1"
                                    herbivoreMin.text = min(herbivoreMin.text.toInt(), herbivoreMax.text.toInt()).toString()
                        }
                        label("-") {
                            minWidth = 10.0
                            alignment = Pos.CENTER
                        }
                        herbivoreMax = textfield("20")
                        herbivoreMax.textProperty().addListener { _, oldValue, newValue -> if (!p.matcher(newValue).matches()) herbivoreMax.text = oldValue }
                        herbivoreMax.focusedProperty().addListener { _, _, isNowFocused ->
                            if (!isNowFocused){
                                if (herbivoreMax.text == "") herbivoreMax.text = "99"
                                herbivoreMax.text = max(herbivoreMin.text.toInt(), herbivoreMax.text.toInt()).toString()
                            }
                        }
                    }
                }
                field("Число хищников (1-99)") {
                    hbox {
                        predatorMin = textfield("3")
                        val p = Pattern.compile("""|[1-9][\d]|[1-9]""")
                        predatorMin.textProperty().addListener { _, oldValue, newValue -> if (!p.matcher(newValue).matches()) predatorMin.text = oldValue }
                        predatorMin.focusedProperty().addListener { _, _, isNowFocused ->
                            if (!isNowFocused)
                                if (predatorMin.text == "") predatorMin.text = "1"
                            predatorMin.text = min(predatorMin.text.toInt(), predatorMax.text.toInt()).toString()
                        }
                        label("-") {
                            minWidth = 10.0
                            alignment = Pos.CENTER
                        }
                        predatorMax = textfield("5")
                        predatorMax.textProperty().addListener { _, oldValue, newValue -> if (!p.matcher(newValue).matches()) predatorMax.text = oldValue }
                        predatorMax.focusedProperty().addListener { _, _, isNowFocused ->
                            if (!isNowFocused){
                                if (predatorMax.text == "") predatorMax.text = "99"
                                predatorMax.text = max(predatorMin.text.toInt(), predatorMax.text.toInt()).toString()
                            }
                        }
                    }
                }
                field {
                    vbox {
                        label("Появление еды")
                        foodProb = slider(0, 100, 20)
                        foodProb.isShowTickLabels = true
                        foodProb.isShowTickMarks = true
                        foodProb.minorTickCount = 1
                        foodProb.valueProperty().addListener { _, _, newValue ->
                            infoLabel.text = """Еды за шаг: ${newValue.toInt()}"""
                        }

                        infoLabel = label("Максимум еды за шаг: ${foodProb.value.toInt()}")
                        infoLabel.textFill = Color.BLUE
                    }
                }
                field {
                    create = button("Создать лес").apply {
                        minWidth = 100.0
                        textFill = Color.BLACK
                        action {
                            start.isSelected = true
                            fire(StartRequest(false))
                            fire(CreateForestRequest(forestWidth.text.toInt(), forestHeight.text.toInt(), herbivoreMin.text.toInt()..herbivoreMax.text.toInt(),predatorMin.text.toInt()..predatorMax.text.toInt(),foodProb.value.toInt()))
                        }
                    }
                }
                field {
                    start = togglebutton {
                        isVisible = false
                        alignment = Pos.CENTER
                        minWidth = 100.0
                        val stateText = selectedProperty().stringBinding {
                            textFill = Color.BLACK
                            if (it == true) "Старт" else "Пауза"
                        }
                        textProperty().bind(stateText)
                        action {
                            fire(StartRequest(!isSelected))
                        }
                        subscribe<CreateForestRequest> {
                            isVisible = true
                        }
                    }
                }
            }
            subscribe<DeadForest> {
                start.isVisible = false
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Лес мертв"
                alert.headerText = null
                alert.contentText =  "Все животные мертвы"
                alert.showAndWait()
            }
        }
        imageview(File("src\\main\\images\\Squirrel.png").toURI().toString()){
            fitWidth = 200.0
            fitHeight = 250.0
            alignment = Pos.CENTER
        }
    }

    init {
        with(root) {
            style { borderColor += box(c("#a1a1a1")) }
            prefWidth = 250.0
        }
    }
}
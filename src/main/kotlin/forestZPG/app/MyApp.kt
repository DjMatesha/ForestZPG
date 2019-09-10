package forestZPG.app

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App
import forestZPG.view.MainView
import java.io.File

class MyApp: App(MainView::class) {
    override fun start(stage: Stage) {
        stage.isResizable = false
        stage.icons += Image(File("src\\main\\images\\icon.png").toURI().toString())
        super.start(stage)
    }
}
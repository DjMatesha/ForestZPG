package forestZPG.view

import tornadofx.*


class CreateForestRequest(val width: Int, val height: Int, val herbivores: IntRange, val predators: IntRange, val foodProb: Int ) : FXEvent(EventBus.RunOn.ApplicationThread)
class StartRequest(val mode: Boolean) : FXEvent(EventBus.RunOn.ApplicationThread)
class IterRequest : FXEvent(EventBus.RunOn.ApplicationThread)
class DeadForest : FXEvent(EventBus.RunOn.ApplicationThread)
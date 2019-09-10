package forestZPG.forest.Interfaces.Сonsumption

import forestZPG.forest.*
import kotlin.math.min
import kotlin.math.truncate

interface PredatorEating : Consuming {
    fun huntValues(prey: Animal): Int = when (prey) {
        is Badger -> 40
        is Chipmunk -> 35
        is FlyingSquirrel -> 25
        is Squirrel -> 30
        is Woodpecker -> 20
        else -> throw Exception("Error prey type")
    }

    override fun eat() {
        if (this is Animal && animalCount > 0) {
            val victims = Forest.animals.filter { it.location == this.location && it::class != this::class }
            for (prey in victims) {
                if (satiety >= 100) {
                    satiety = 100.0
                    break
                }
                val hungriness = this.groupHungriness
                val foodValue = huntValues(prey)
                val toEat = min(truncate(hungriness / foodValue).toInt(), prey.animalCount)
                prey.animalCount -= toEat
                prey.updateText()
                feed(toEat * foodValue)
            }
        } else throw IllegalArgumentException("Animal is required")
    }
}
package forestZPG.forest

import forestZPG.forest.Enums.AnimalType
import forestZPG.forest.Enums.Food
import forestZPG.forest.Enums.TreeSection
import forestZPG.forest.Interfaces.Сonsumption.Consuming
import forestZPG.forest.Interfaces.Сonsumption.HerbivorousEating
import forestZPG.forest.Interfaces.Сonsumption.PredatorEating
import forestZPG.forest.Interfaces.Movement.Climbing
import forestZPG.forest.Interfaces.Movement.Flying
import forestZPG.forest.Interfaces.Movement.Moving
import forestZPG.forest.Interfaces.Movement.Walking
import kotlin.math.max
import kotlin.random.Random

fun Double.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)

@Suppress("LeakingThis")
sealed class Animal : Consuming, Moving {
    var location: TreePart = getPossibleLocation()
    abstract val eatLocation: TreeSection
    private var stamina = 50.0
    open var satiety = 50.0
    var animalCount = 1
    protected abstract val childProb: Int
    protected abstract val fellowship: Int


    private val image = GameObject(this)

    val groupHungriness get() = (100 - satiety) * animalCount

    fun feed(foodPoints: Int) {
        satiety += foodPoints / animalCount
    }

    override fun toString(): String = "$animalCount units\nSatiety: [${satiety.format(2)}/100]\nStamina: [${stamina.format(2)}/100]\n"

    fun progeny() {
        if (animalCount >= 2 && Random.nextInt(100) < childProb * satiety / 100) {
            animalCount += Random.nextInt(1, animalCount / 2 + 1)
            updateText()
        }
    }

    fun discord(): Animal? {
        if (animalCount > fellowship && Random.nextInt(0, 40) >= fellowship) {
            val part: Int = animalCount / 2
            animalCount -= part
            updateText()
            return copy(part)
        }
        return null
    }

    fun makeMove() {
        val newLocation = getPossibleLocation()
        val dist = location.dist(newLocation)
        val cost = staminaCost(dist)
        if (stamina > cost) {
            stamina -= cost
            satiety -= dist + 5
            image.drawMove(this, newLocation)
            location = newLocation
        } else {
            stamina = max(stamina+30,100.0)
            satiety -= 5
        }
    }

    fun updateText() = image.setText(animalCount.toString())

    fun canUnite(animal: Animal) = (!(this === animal) && location == animal.location && this::class == animal::class)

    fun removeAnimalImage() {
        if (animalCount == 0) image.remove()
    }

    fun removeAnimalIfStarve() {
        if (satiety <= 0) {
            satiety = 0.0
            if (animalCount > 0) animalCount = Random.nextInt(animalCount)
            updateText()
            this.removeAnimalImage()
        }
    }

    operator fun plusAssign(animal: Animal) {
        satiety = (satiety * animalCount + animal.satiety * animal.animalCount) / (animalCount + animal.animalCount)
        stamina = (stamina * animalCount + animal.stamina * animal.animalCount) / (animalCount + animal.animalCount)
        animalCount += animal.animalCount
        updateText()
        animal.animalCount = 0
    }

    @Suppress("UNCHECKED_CAST")
    companion object AnimalFactory {
        fun createAnimal(type: AnimalType): Animal = when (type) {
            AnimalType.SQUIRREL -> Squirrel()
            AnimalType.FLYING_SQUIRREL -> FlyingSquirrel()
            AnimalType.WOODPECKER -> Woodpecker()
            AnimalType.CHIPMUNK -> Chipmunk()
            AnimalType.BADGER -> Badger()
            AnimalType.WOLF -> Wolf()
            AnimalType.VULTURE -> Vulture()
        }

        fun <T : Animal> T.copy(count: Int): T {
            val childAnimal = when (this) {
                is Squirrel -> Squirrel()
                is FlyingSquirrel -> FlyingSquirrel()
                is Woodpecker -> Woodpecker()
                is Chipmunk -> Chipmunk()
                is Badger -> Badger()
                is Wolf -> Wolf()
                is Vulture -> Vulture()
                else -> throw IllegalArgumentException("Wrong animal type")
            } as T
            childAnimal.location = this.location
            childAnimal.animalCount = count
            childAnimal.stamina = this.stamina
            childAnimal.satiety = this.satiety
            updateText()
            return childAnimal
        }
    }
}


class Squirrel : Animal(), HerbivorousEating, Climbing {
    override val childProb = 15
    override val fellowship = 20
    override val eatLocation: TreeSection = TreeSection.CROWN
    override val acceptableFood: Set<Food> = setOf(Food.NUTS, Food.CONES)
    override fun toString(): String {
        return "Squirrel\n"+super.toString()
    }
}

class FlyingSquirrel : Animal(), HerbivorousEating, Climbing {
    override val childProb = 15
    override val fellowship = 16
    override val eatLocation: TreeSection = TreeSection.CROWN
    override val acceptableFood: Set<Food> = setOf(Food.NUTS, Food.MAPLE_LEAVES, Food.CATKINS)
    override fun toString(): String {
        return "FlyingSquirrel\n"+super.toString()
    }
}

class Woodpecker : Animal(), HerbivorousEating, Climbing {
    override val childProb = 10
    override val fellowship = 7
    override val eatLocation: TreeSection = TreeSection.TRUNK
    override val acceptableFood: Set<Food> = setOf(Food.WORMS)
    override fun toString(): String {
        return "Woodpecker\n"+super.toString()
    }
}

class Chipmunk : Animal(), HerbivorousEating, Climbing {
    override val childProb = 10
    override val fellowship = 10
    override val eatLocation: TreeSection = TreeSection.ROOTS
    override val acceptableFood: Set<Food> = setOf(Food.NUTS, Food.CONES)
    override fun toString(): String {
        return "Chipmunk\n"+super.toString()
    }
}

class Badger : Animal(), HerbivorousEating, Walking {
    override val childProb = 15
    override val fellowship = 8
    override val eatLocation: TreeSection = TreeSection.ROOTS
    override val acceptableFood: Set<Food> = setOf(Food.ROOT_VEGETABLES)
    override fun toString(): String {
        return "Badger\n"+super.toString()
    }
}

class Wolf : Animal(), PredatorEating, Walking {
    override val childProb = 5
    override val fellowship = 15
    override val eatLocation: TreeSection = TreeSection.ROOTS
    override fun toString(): String {
        return "Wolf\n"+super.toString()
    }
}

class Vulture : Animal(), PredatorEating, Flying {
    override val childProb = 5
    override val fellowship = 5
    override val eatLocation: TreeSection = TreeSection.CROWN
    override fun toString(): String {
        return "Vulture\n"+super.toString()
    }
}
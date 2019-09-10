package forestZPG.forest.Interfaces.Сonsumption

import forestZPG.forest.Animal
import forestZPG.forest.Enums.Food
import kotlin.math.min
import kotlin.math.truncate

interface HerbivorousEating : Consuming {
    val acceptableFood: Set<Food>
    fun foodValues(food: Food) = when (food) {
        Food.ROOT_VEGETABLES -> 15
        Food.CONES -> 10
        Food.CATKINS -> 9
        Food.MAPLE_LEAVES -> 8
        Food.NUTS -> 20
        Food.WORMS -> 13
    }

    override fun eat() {
        if (this is Animal) {
            if ((animalCount > 0) && (satiety < 100) && (location.tree.getLocation(eatLocation) == location) && (location.produceFood in acceptableFood)) {
                val hungriness = this.groupHungriness
                val foodValue = foodValues(location.produceFood)
                val toEat = min(truncate(hungriness / foodValue).toInt(), location.foodAmount)
                location.foodAmount -= toEat
                feed(toEat * foodValue)
            }
        } else throw IllegalArgumentException("Animal is required")
    }
}
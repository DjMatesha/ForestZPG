package forestZPG.forest.Interfaces.Movement

import forestZPG.forest.Enums.TreeSection
import forestZPG.forest.Forest

interface Moving {
    val availablePlaces: List<TreeSection>

    fun staminaCost(dist: Int): Int
    fun getPossibleLocation() = Forest.randomTree().getLocation(availablePlaces.random())
}
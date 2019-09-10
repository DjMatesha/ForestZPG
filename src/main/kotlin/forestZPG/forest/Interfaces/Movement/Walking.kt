package forestZPG.forest.Interfaces.Movement

import forestZPG.forest.Enums.TreeSection


interface Walking : Moving {
    override fun staminaCost(dist: Int) = dist*3
    override val availablePlaces: List<TreeSection>
        get() = listOf(TreeSection.ROOTS)
}
package forestZPG.forest.Interfaces.Movement

import forestZPG.forest.Enums.TreeSection

interface Climbing : Moving {
    override fun staminaCost(dist: Int) = dist*6
    override val availablePlaces: List<TreeSection>
        get() = listOf(TreeSection.CROWN, TreeSection.ROOTS, TreeSection.TRUNK)
}
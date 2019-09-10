package forestZPG.forest.Interfaces.Movement

import forestZPG.forest.Enums.TreeSection

interface Flying : Moving {
    override fun staminaCost(dist: Int) = dist*4
    override val availablePlaces: List<TreeSection>
        get() = listOf(TreeSection.CROWN)
}
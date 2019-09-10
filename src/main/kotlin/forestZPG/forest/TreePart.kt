package forestZPG.forest

import forestZPG.forest.Enums.Food
import kotlin.math.absoluteValue
import kotlin.random.Random

sealed class TreePart(val tree: Tree) {
    private val eatPerTick = Forest.eatPerTick
    var foodAmount: Int = eatPerTick * Random.nextDouble(2.0, 10.0).toInt()
    abstract val produceFood: Food
    fun produce() {
        foodAmount += Random.nextInt(eatPerTick)
    }

    fun dist(newLoc: TreePart): Int {
        val oldTree = tree
        val newTree = newLoc.tree
        return (newTree.coordinates.first - oldTree.coordinates.first).absoluteValue + (newTree.coordinates.second - oldTree.coordinates.second).absoluteValue
    }

    override fun toString(): String = "$produceFood $foodAmount"
}

class Crown(tree: Tree) : TreePart(tree) {
    override val produceFood =
            when (tree) {
                is Walnut -> Food.NUTS
                is Pine -> Food.CONES
                is Birch -> Food.CATKINS
                is Fir -> Food.CONES
                is Maple -> Food.MAPLE_LEAVES
                is Oak -> Food.NUTS
            }
}

class Trunk(tree: Tree) : TreePart(tree) {
    override val produceFood = Food.WORMS
}

class Roots(tree: Tree) : TreePart(tree) {
    override val produceFood = Food.ROOT_VEGETABLES
}
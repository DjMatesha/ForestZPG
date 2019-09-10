package forestZPG.forest

import forestZPG.forest.Enums.TreeSection
import forestZPG.forest.Enums.TreeType

@Suppress("LeakingThis")
sealed class Tree {
    abstract val coordinates: Pair<Int, Int>
    fun getLocation(part: TreeSection) = when (part) {
        TreeSection.CROWN -> crown
        TreeSection.ROOTS -> roots
        TreeSection.TRUNK -> trunk
    }

    private val crown = Crown(this)
    private val trunk = Trunk(this)
    private val roots = Roots(this)

    fun produce() {
        crown.produce()
        trunk.produce()
        roots.produce()
    }

    fun createReport(): String = this.toString()+"\n$crown\n$trunk\n$roots"

    companion object TreeFactory {
        private fun createTree(type: TreeType, coordinates: Pair<Int, Int>): Tree = when (type) {
            TreeType.FIR -> Fir(coordinates)
            TreeType.PINE -> Pine(coordinates)
            TreeType.OAK -> Oak(coordinates)
            TreeType.MAPLE -> Maple(coordinates)
            TreeType.BIRCH -> Birch(coordinates)
            TreeType.WALNUT -> Walnut(coordinates)
        }

        fun createTree(coordinates: Pair<Int, Int>): Tree = createTree(TreeType.values().random(), coordinates)
    }
}

class Fir(override val coordinates: Pair<Int, Int>) : Tree() {
    override fun toString()= "Fir"
}

class Pine(override val coordinates: Pair<Int, Int>) : Tree() {
    override fun toString(): String {
        return "Pine"
    }
}

class Oak(override val coordinates: Pair<Int, Int>) : Tree() {
    override fun toString(): String {
        return "Oak"
    }
}

class Maple(override val coordinates: Pair<Int, Int>) : Tree() {
    override fun toString(): String {
        return "Maple"
    }
}

class Birch(override val coordinates: Pair<Int, Int>) : Tree() {
    override fun toString(): String {
        return "Birch"
    }
}

class Walnut(override val coordinates: Pair<Int, Int>) : Tree() {
    override fun toString(): String {
        return "Walnut"
    }
}
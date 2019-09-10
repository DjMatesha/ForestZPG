package forestZPG.forest.Enums

enum class Food {
    NUTS,
    CONES,
    WORMS,
    MAPLE_LEAVES,
    ROOT_VEGETABLES,
    CATKINS;

    override fun toString() =  name[0] +
            name.slice(1 until name.length).map { it.toLowerCase() }.joinToString("")
}
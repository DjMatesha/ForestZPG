package forestZPG.forest

import forestZPG.forest.Enums.AnimalType
import kotlin.random.Random

fun <T> List<List<T>>.random(): T = this[Random.nextInt(this.count())][Random.nextInt(this[0].count())]

object Forest {
    var height: Int = 0
    var width: Int = 0
    var eatPerTick = 20
    var woodland: List<List<Tree>> = listOf()
    val animals: MutableList<Animal> = mutableListOf()
    private var iteration = 1

    fun initForest(Row: Int, Col: Int, herbivores: IntRange, predators: IntRange, foodProb: Int) {
        eatPerTick = foodProb
        height = Row
        width = Col
        iteration = 1
        DrawPart.drawUpdate()
        woodland = (0 until Row).map { i ->
            List(Col) { j ->
                Tree.createTree(Pair(i, j)).also { tree -> DrawPart.drawTree(tree) }
            }
        }

        Forest.animals.clear()
        repeat(herbivores.random()) {
            animals += Animal.createAnimal(AnimalType.BADGER)
        }
        repeat(herbivores.random()) {
            animals += Animal.createAnimal(AnimalType.CHIPMUNK)
        }
        repeat(herbivores.random()) {
            animals += Animal.createAnimal(AnimalType.FLYING_SQUIRREL)
        }
        repeat(herbivores.random()) {
            animals += Animal.createAnimal(AnimalType.SQUIRREL)
        }
        repeat(herbivores.random()) {
            animals += Animal.createAnimal(AnimalType.WOODPECKER)
        }
        repeat(predators.random()) {
            animals += Animal.createAnimal(AnimalType.WOLF)
        }
        repeat(predators.random()) {
            animals += Animal.createAnimal(AnimalType.VULTURE)
        }
        union()

    }

    fun isDead() = animals.isEmpty()

    fun makeIteration() {
        iteration++
        produceFood()
        produceFood()
        animals.forEach { it.eat() }
        updateGeneration()
        discord()
        animals.forEach { it.makeMove() }
        union()
    }

    private fun union() {
        for (i in animals)
            for (j in animals)
                if (i.canUnite(j) && i.animalCount != 0)
                    i+=j
        animals.forEach { it.removeAnimalImage() }
        animals.removeIf { animal -> animal.animalCount == 0 }
    }

    private fun produceFood() {
        woodland.forEach { row -> row.forEach { cell -> cell.produce() } }
    }

    private fun discord() {
        val list: MutableList<Animal> = mutableListOf()
        animals.forEach { animals -> animals.discord()?.let { list.add(it) } }
        animals.addAll(list)
    }

    private fun updateGeneration() {
        animals.forEach { it.removeAnimalImage() }
        animals.removeIf { animal -> animal.animalCount == 0 }
        animals.forEach { animal -> animal.progeny() }
        animals.forEach { animal -> animal.removeAnimalIfStarve() }
        animals.removeIf { animal -> animal.animalCount == 0 }
    }

    fun randomTree() = woodland.random()
}
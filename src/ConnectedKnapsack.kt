import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

class ConnectedKnapsack(
    private val weights: Array<Float>, private val values: Array<Float>, private val adjacencyMatrix: Array<Array<Boolean>>,
    private val knapsackCapacity: Float, tabuRate: Float, iterationsRate: Float, private val randomSeed: Int) {

    private val amountOfElements = weights.size
    private var currentSolution = Array(amountOfElements) { false }
    private var bestSolution = currentSolution.clone()
    private val tabuArray = Array(amountOfElements) { 0 }
    private val elementsRange = (0 until amountOfElements)
    private val amountOfIterations = ceil(amountOfElements * iterationsRate).toInt()

    private val TABU_ITERATIONS = ceil(amountOfElements * tabuRate).toInt()
    private val NUMBER_OF_THREADS = 8

    init {
        generateInitialSolution()
        bestSolution = currentSolution
    }

    fun tabuSearch(): Float {
        println("Starting Tabu Search...")
        var iterationCounter = 0
        while (iterationCounter < amountOfIterations) {
            if (iterationCounter % 100 == 0) {
                println("Starting iteration $iterationCounter")
            }
            val neighbors = generateNeighbors()
            val values = neighbors.filter { n -> neighborIsValid(n) }.sortedByDescending { n -> selectionValue(n.first) }
            try {
                val bestNeighbor = values.first { n -> subGraphIsConnected(n.first) }
                currentSolution = bestNeighbor.first
                tabuArray[bestNeighbor.second] = TABU_ITERATIONS
                if (selectionValue(currentSolution) > selectionValue(bestSolution)) {
                    bestSolution = currentSolution.clone()
                }
            } catch (e: NoSuchElementException) {}
            iterationCounter++
        }
        return selectionValue(bestSolution)
    }

    private fun generateInitialSolution() {
        val firstItemIndex = Random(randomSeed).nextInt(amountOfElements)
        val firstSolution = Array(amountOfElements) { false }
        firstSolution[firstItemIndex] = true
        while (firstSolution.toList() != currentSolution.toList() && fitInKnapsack(firstSolution)) {
            currentSolution = firstSolution.clone()
            var itemIndex = 0
            for (item in firstSolution) {
                if (item) {
                    var adjacencyIndex = 0
                    for (adjacency in adjacencyMatrix[itemIndex]) {
                        if (adjacency) {
                            firstSolution[adjacencyIndex] = true
                        }
                        adjacencyIndex += 1
                    }
                }
                itemIndex += 1
            }
        }
    }

    private fun generateNeighbors(): List<Pair<Array<Boolean>, Int>> {
        val neighbors = mutableListOf<Pair<Array<Boolean>, Int>>()
        for (index in elementsRange) {
            val neighbor = currentSolution.clone()
            neighbor[index] = neighbor[index].not()
            neighbors.add(Pair(neighbor, index))
        }
        return neighbors
    }

    private fun indexesPairs(): MutableList<Pair<Int, Int>> {
        val ranges = mutableListOf<Pair<Int, Int>>()
        val rangeSize = amountOfElements / NUMBER_OF_THREADS
        for (i in 1..NUMBER_OF_THREADS) {
            ranges.add(Pair(floor((i - 1).toDouble() * rangeSize).toInt(), ceil(i.toDouble() * rangeSize).toInt()))
        }
        return ranges
    }

    private fun calculateValues(neighbors: List<Pair<Array<Boolean>, Int>>): List<Triple<Boolean, Float, Int>> {
        val values = mutableListOf<Triple<Boolean, Float, Int>>()
        for (neighbor in neighbors) {
            if (neighborIsValid(neighbor)) {
                values.add(Triple(true, selectionValue(neighbor.first), neighbor.second))
            } else {
                values.add(Triple(false, 0F, neighbor.second))
            }
        }
        return values
    }

    private fun neighborIsValid(neighbor: Pair<Array<Boolean>, Int>): Boolean {
        if (tabuArray[neighbor.second] > 0) {
            tabuArray[neighbor.second]--
            return false
        }
        return fitInKnapsack(neighbor.first)
    }

    private fun fitInKnapsack(selectionArray: Array<Boolean>): Boolean {
        return selectionWeight(selectionArray) <= knapsackCapacity
    }

    private fun subGraphIsConnected(selectionArray: Array<Boolean>): Boolean {
        return Graph(adjacencyMatrix).subgraphIsConnected(selectionArray)
    }

    private fun selectionValue(selectionArray: Array<Boolean>): Float = selectionSum(selectionArray, values)

    private fun selectionWeight(selectionArray: Array<Boolean>): Float = selectionSum(selectionArray, weights)

    private fun selectionSum(selectionArray: Array<Boolean>, valuesArray: Array<Float>): Float {
        var sum = 0F
        for (index in elementsRange) {
            if (selectionArray[index]) {
                sum += valuesArray[index]
            }
        }
        return sum
    }
}
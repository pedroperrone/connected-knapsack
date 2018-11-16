
class ConnectedKnapsack(val weights: Array<Float>, val values: Array<Float>, val adjacencyMatrix: Array<Array<Boolean>>,
                        val knapsackCapacity: Float) {

    val TABU_ITERATIONS = 3

    val amountOfElements = weights.size
    var currentSolution = Array(amountOfElements) { false }
    var bestSolution = currentSolution.clone()
    val tabuArray = Array(amountOfElements) { 0 }
    val elementsRange = (0..amountOfElements - 1)

    fun tabuSearch(): Float {
        println("Starting Tabu Search...")
        var iterationCounter = 0
        while (iterationCounter < amountOfElements) {
            if (iterationCounter % 100 == 0) {
                println("Starting iteration $iterationCounter")
            }
            val neighbors = generateNeighbors()
            val values = calculateValues(neighbors)
            val sortedValidNeighbors = values.filter { v -> v.first }.sortedByDescending { v -> v.second }
            if (!sortedValidNeighbors.isEmpty()) {
                val bestNeighbor = sortedValidNeighbors[0]
                currentSolution = neighbors.find { n -> n.second == bestNeighbor.third }!!.first.clone()
                tabuArray[bestNeighbor.third] = TABU_ITERATIONS
                if (selectionValue(currentSolution) > selectionValue(bestSolution)) {
                    bestSolution = currentSolution.clone()
                }
            }
            iterationCounter++
        }
        return selectionValue(bestSolution)
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
        if (!fitInKnapsack(neighbor.first)) {
            return false
        }
        return subGraphIsConnected(neighbor.first)
    }

    private fun fitInKnapsack(selectionArray: Array<Boolean>): Boolean {
        return selectionWeight(selectionArray) <= knapsackCapacity
    }

    private fun subGraphIsConnected(selectionArray: Array<Boolean>): Boolean {
        return true
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
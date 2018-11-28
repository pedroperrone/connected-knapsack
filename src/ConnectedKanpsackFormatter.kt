import java.io.File

class ConnectedKanpsackFormatter {
    companion object {
        fun fromFile(fileName: String): List<Any> {
            val lines = mutableListOf<String>()
            File(fileName).forEachLine { line -> lines.add(line) }
            val firstLine = lines.removeAt(0).split(" ")
            val amountOfElements = firstLine[0].toInt()
            val knapsackWeight = firstLine[2].toFloat()

            val weights = lines.removeAt(0).split(" ").filter { s -> !s.isEmpty() }.map { weight -> weight.toFloat() }
            val values = lines.removeAt(0).split(" ").filter { s -> !s.isEmpty() }.map { value -> value.toFloat() }

            val adjacencyMatrix = createAdjacencyMatrix(lines, amountOfElements)

            return listOf(weights, values, adjacencyMatrix, knapsackWeight)
        }

        private fun createAdjacencyMatrix(lines: MutableList<String>, matrixSize: Int): List<List<Boolean>> {
            val adjacencyMatrix = MutableList(matrixSize) { MutableList(matrixSize) { false } }
            for (line in lines) {
                val parsedLine = line.split(" ").map { index -> index.toInt() }
                adjacencyMatrix[parsedLine[0]][parsedLine[1]] = true
                adjacencyMatrix[parsedLine[1]][parsedLine[0]] = true
            }
            return adjacencyMatrix
        }

        fun output(fileName: String, fileContent: List<Int>) {
            File(fileName).writeText(fileContent.toString())
        }
    }
}

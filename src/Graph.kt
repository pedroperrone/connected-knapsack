class Graph(val adjacencyMatrix: Array<Array<Boolean>>) {
    val graphSize = adjacencyMatrix.size
    val vertexesRange = 0 until graphSize
    val openedVertexes = mutableListOf<Int>()
    val closedVertexes = mutableListOf<Int>()

    fun isConnected(): Boolean {
        expand(0)
        return vertexesRange.toList() == closedVertexes.sorted()
    }

    private fun expand(vertex: Int) {
        for (adjacency in vertexesRange) {
            if (adjacencyMatrix[vertex][adjacency]) {
                addToOpened(adjacency)
            }
        }
        closedVertexes.add(vertex)
        if (openedVertexes.isEmpty()) {
            return
        }
        expand(openedVertexes.removeAt(0))
    }

    private fun addToOpened(vertex: Int) {
        if (openedVertexes.contains(vertex)) {
            return
        }
        if (closedVertexes.contains(vertex)) {
            return
        }
        openedVertexes.add(vertex)
    }
}
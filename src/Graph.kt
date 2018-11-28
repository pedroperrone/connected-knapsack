class Graph(val adjacencyMatrix: Array<Array<Boolean>>) {
    val graphSize = adjacencyMatrix.size
    val vertexesRange = 0 until graphSize
    val openedVertexesList = mutableListOf<Int>()
    val openedVertexes = Array(graphSize) { false }
    val closedVertexes = Array(graphSize) { false }

    fun isConnected(): Boolean {
        expand(0)
        return closedVertexes.all { v -> v }
    }

    private fun expand(vertex: Int) {
        for (adjacency in vertexesRange) {
            if (adjacencyMatrix[vertex][adjacency]) {
                addToOpened(adjacency)
            }
        }
        closedVertexes[vertex] = true
        if (openedVertexesList.isEmpty()) {
            return
        }
        expand(openedVertexesList.removeAt(0))
    }

    private fun addToOpened(vertex: Int) {
        if (openedVertexes[vertex]) {
            return
        }
        if (closedVertexes[vertex]) {
            return
        }
        openedVertexes[vertex] = true
        openedVertexesList.add(vertex)
    }

    fun subgraphIsConnected(subgraphSelection: Array<Boolean>): Boolean {
        if (subgraphSelection.filter { v -> v }.isEmpty()) {
            return false
        }
        return generateSubgraph(subgraphSelection).isConnected()
    }

    private fun generateSubgraph(subgraphSelection: Array<Boolean>): Graph {
        val subgraphSize = subgraphSelection.count { v -> v }
        val subgraphMatrix = Array(subgraphSize) { Array(subgraphSize) { false } }
        var subgraphVertex = 0
        for (vertex in vertexesRange) {
            if (subgraphSelection[vertex]) {
                var subgraphAdjacency = 0
                for (adjacency in vertexesRange) {
                    if (subgraphSelection[adjacency]) {
                        subgraphMatrix[subgraphVertex][subgraphAdjacency] = adjacencyMatrix[vertex][adjacency]
                        subgraphAdjacency++
                    }
                }
                subgraphVertex++
            }
        }
        return Graph(subgraphMatrix)
    }
}
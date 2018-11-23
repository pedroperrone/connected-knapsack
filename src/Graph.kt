class Graph(val adjacencyMatrix: Array<Array<Boolean>>) {
    val graphSize = adjacencyMatrix.size
    val vertexesRange = 0 until graphSize
    val openedVertexes = mutableListOf<Int>()
    val closedVertexes = mutableListOf<Int>()

    fun isConnected(): Boolean {
        if (closedVertexes.isEmpty()) {
            expand(0)
        }
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

    fun subgraphIsConnected(subgraphSelection: Array<Boolean>): Boolean {
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
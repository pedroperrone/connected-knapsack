fun main(args: Array<String>) {
    val instanceParams = ConnectedKanpsackFormatter.fromFile(args[0])
    println("Pesos: ${instanceParams[0]}")
    println("Valores: ${instanceParams[1]}")
    println("Grafo: ${instanceParams[2]}")
    println("Peso da mochila: ${instanceParams[3]}")
    val problemInstance = ConnectedKnapsack(instanceParams[0] as FloatArray, instanceParams[1] as FloatArray,
                                            instanceParams[2] as Array<BooleanArray>, instanceParams[3] as Float)
    problemInstance.tabuSearch()
}

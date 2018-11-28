import java.io.FileNotFoundException
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    lateinit var instanceParams: List<Any>
    try {
        println("Reading input")
        instanceParams = ConnectedKanpsackFormatter.fromFile(args[1])
    } catch (e: FileNotFoundException) {
        println("No file found with the name given as parameter.")
        return
    }
    val weights = (instanceParams[0] as List<Float>).toTypedArray()
    val values = (instanceParams[1] as List<Float>).toTypedArray()
    val adjacencyMatrix = (instanceParams[2] as List<List<Boolean>>).map { row -> row.toTypedArray() }.toTypedArray()
    val knapsackCapacity = instanceParams[3] as Float

    lateinit var result: Pair<Float, List<Int>>
    val time = measureTimeMillis {
        val problemInstance = ConnectedKnapsack(weights, values, adjacencyMatrix, knapsackCapacity,
            args[2].toFloat(), args[3].toFloat(), args[4].toInt())
        result = problemInstance.tabuSearch()
        println(result.first)
    }
    println("Execution took $time milliseconds to run.")
    println("Writing result in ${args[0]}")
    ConnectedKanpsackFormatter.output(args[0], result.second)
}
import java.io.FileNotFoundException
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    lateinit var instanceParams: List<Any>
    try {
        println("Reading input")
        instanceParams = ConnectedKanpsackFormatter.fromFile(args[0])
    } catch (e: FileNotFoundException) {
        println("No file found with the name given as parameter.")
        return
    }
    val weights = (instanceParams[0] as List<Float>).toTypedArray()
    val values = (instanceParams[1] as List<Float>).toTypedArray()
    val adjacencyMatrix = (instanceParams[2] as List<List<Boolean>>).map { row -> row.toTypedArray() }.toTypedArray()
    val knapsackCapacity = instanceParams[3] as Float
    val time = measureTimeMillis {
        val problemInstance = ConnectedKnapsack(weights, values, adjacencyMatrix, knapsackCapacity)
        println(problemInstance.tabuSearch())
    }
    println("Execution took $time milliseconds to run.")
}
import com.codingame.BeamSearchAlgorithm
import com.codingame.Path
import com.codingame.rushhour.*
import java.util.*

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 */
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt() // Number of vehicles

    var path = resolvePath(input, n)
    path = consumeNextAction(path)

    // game loop
    while (true) {
        parseState(input, n)
        path = consumeNextAction(path)
    }
}

fun resolvePath(input: Scanner, vehicleCount: Int): Path {
    val initialState = parseState(input, vehicleCount)
    val searchAlgorithm = BeamSearchAlgorithm(initialState, width = 5)
    return searchAlgorithm.findPathToFinalState()
}

fun consumeNextAction(path: Path): Path {
    println(path[0].action)
    return path.subPath(1)
}

private fun parseState(input: Scanner, vehicleCount: Int): State {
    val vehiclesCoordinates = sequence {
        for (i in 0 until vehicleCount) {
            val id = input.nextInt() // Id of the vehicle
            val x = input.nextInt() // Horizontal coordinate of the vehicle
            val y = input.nextInt() // Vertical coordinate of the vehicle
            val coordinates = Coordinates(x, y)
            val length = input.nextInt() // Length of the vehicle, in cells
            val axis = input.next() // Axis of the vehicle : H (horizontal) or V (vertical)
            val vehicle = Vehicle(id, length, Axis.fromString(axis))
            yield(vehicle to coordinates)
        }
    }.toMap()

    return State(vehiclesCoordinates)
}

fun debug(x: Any) {
    System.err.println(x)
}

import com.codingame.rushhour.*
import java.util.*

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 */
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt() // Number of vehicles

    // game loop
    while (true) {

        val vehiclesCoordinates = sequence {
            for (i in 0 until n) {
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

        val initialState = State(vehiclesCoordinates)
        debug(initialState)
        val actionResolver = ActionResolver(initialState)
        val action = actionResolver.resolveNextAction()
        println(action)
    }
}

fun debug(x: Any) {
    System.err.println(x)
}

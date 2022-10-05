import java.io.*
import java.math.*
import java.util.*

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 */
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = input.nextInt() // Number of vehicles

    // game loop
    var step = 0
    while (true) {
        for (i in 0 until n) {
            val id = input.nextInt() // Id of the vehicle
            val x = input.nextInt() // Horizontal coordinate of the vehicle
            val y = input.nextInt() // Vertical coordinate of the vehicle
            val length = input.nextInt() // Length of the vehicle, in cells
            val axis = input.next() // Axis of the vehicle : H (horizontal) or V (vertical)
        }

        if (step <= 0) {
            println("1 DOWN") // ID DIRECTION
        } else {
            println("0 RIGHT")
        }

        ++step
    }
}

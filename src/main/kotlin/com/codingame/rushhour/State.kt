package com.codingame.rushhour

import com.codingame.DirectedEdge
import com.codingame.Node
import debug

val EXIT = Coordinates(Coordinates.MAX.x, Coordinates.MAX.y / 2)

data class State(val vehicleCoordinates: Map<Vehicle, Coordinates>) : Node.State {
    private fun with(vehicle: Vehicle): Builder {
        return Builder(vehicle)
    }

    private fun areEmpty(coordinates: Coordinates): Boolean {
        return (coordinates !in notEmptyCoordinatesExceptRedVehicle)
    }

    private val notEmptyCoordinatesExceptRedVehicle: Set<Coordinates> by lazy {
        vehicleCoordinates
            .filterNot { it.key == redVehicle }
            .flatMap { (vehicle, coordinates) -> vehicle.allCoordinatesFrom(coordinates) }.toSet()
    }

    inner class Builder(private val vehicle: Vehicle) {

        /**
         * Renvoie l'état initial si état suivant impossible/invalide.
         */
        fun at(nextCoordinates: Coordinates): State {
            val newVehicleCoordinates = HashMap(vehicleCoordinates)
            newVehicleCoordinates[vehicle] = nextCoordinates
            return State(newVehicleCoordinates)
        }

    }

    override val isFinal: Boolean get() = redVehicleDistanceFromExit == 0

    private val redVehicle: Vehicle by lazy {
        vehicleCoordinates.keys.first { it.id == 0 }
    }

    private val redVehicleDistanceFromExit by lazy {
        vehicleCoordinates[redVehicle]!!.distanceFrom(
            EXIT,
            redVehicle.axis
        ) - (redVehicle.length - 1)
    }

    override val nextPossibleStates: Iterable<Pair<DirectedEdge.Action, Node.State>>
        get() = computeNextPossibleStates()

    private fun computeNextPossibleStates(): Iterable<Pair<DirectedEdge.Action, Node.State>> {
        return sequence {
            vehicleCoordinates.forEach { (vehicle, _) ->
                Direction.values()
                    .filter { it.isOn(vehicle.axis) }
                    .map { direction ->
                        val previousCoordinates = vehicleCoordinates[vehicle]
                        if (previousCoordinates != null) {
                            val nextCoordinates = previousCoordinates.next(direction)
                            val coordinatesToValidate = when (direction) {
                                Direction.UP, Direction.LEFT -> nextCoordinates
                                Direction.RIGHT, Direction.DOWN -> vehicle.maxCoordinatesFrom(nextCoordinates)
                            }
                            if (coordinatesToValidate.areValid && areEmpty(coordinatesToValidate)) {
                                val nextState = with(vehicle).at(nextCoordinates)
                                yield(Action(vehicle.id, direction) to nextState)
                            }
                        }
                    }
            }
        }.toList()
    }

    override val minimumCostToFinalState: Int get() = redVehicleDistanceFromExit // TODO prendre en compte les vehicules barrant la route

    override fun toString(): String {
        return sequence {
            for (y in Coordinates.MIN.y..Coordinates.MAX.y) {
                for (x in Coordinates.MIN.x..Coordinates.MAX.x) {
                    val coordinates = Coordinates(x, y)
                    yield(
                        if (areEmpty(coordinates)) {
                            ' '
                        } else {
                            '#'
                        }
                    )
                }
                yield('\n')
            }
        }.joinToString("")
    }
}

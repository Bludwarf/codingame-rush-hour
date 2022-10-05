package com.codingame.rushhour

import debug

class ActionResolver(val initialState: State) {

    fun resolveNextAction(): Action {

        val nextStates = sequence {
            initialState.vehicleCoordinates.forEach { (vehicle, _) ->
                Direction.values()
                    .filter { vehicle.canGo(it) }
                    .map { direction ->
                        val previousCoordinates = initialState.vehicleCoordinates[vehicle]
                        if (previousCoordinates != null) {
                            val nextCoordinates = previousCoordinates.next(direction)
                            val coordinatesToValidate = when(direction) {
                                Direction.UP, Direction.LEFT -> nextCoordinates
                                Direction.RIGHT, Direction.DOWN -> vehicle.maxCoordinatesFrom(nextCoordinates)
                            }
                            debug("Coordinates to validate $coordinatesToValidate")
                            if (coordinatesToValidate.areValid && initialState.areEmpty(coordinatesToValidate, vehicle)) {
                                val nextState = initialState.with(vehicle).at(nextCoordinates)
                                yield(Action(vehicle.id, direction) to nextState)
                            }
                        }
                    }
            }
        }.toMap()
        debug(nextStates)

        // TODO
        return Action(1, Direction.DOWN)
    }

}

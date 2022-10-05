package com.codingame.rushhour

data class State(val vehicleCoordinates: Map<Vehicle, Coordinates>) {
    fun with(vehicle: Vehicle): Builder {
        return Builder(vehicle)
    }

    fun areEmpty(coordinates: Coordinates, excludedVehicle: Vehicle): Boolean {
        return vehicleCoordinates
            .filter { (vehicle, _) -> vehicle != excludedVehicle }
            .none { (vehicle, vehicleCoordinates) ->
                vehicle.allCoordinatesFrom(vehicleCoordinates).any { it == coordinates }
            }
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
}

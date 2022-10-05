package com.codingame.rushhour

data class Action(val vehicleId: Int, val direction: Direction) {
    override fun toString(): String {
        return "$vehicleId $direction"
    }
}

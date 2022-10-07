package com.codingame.rushhour

import com.codingame.DirectedEdge

data class Action(val vehicleId: Int, val direction: Direction): DirectedEdge.Action {
    override fun toString(): String {
        return "$vehicleId $direction"
    }
}

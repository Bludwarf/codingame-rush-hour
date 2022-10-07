package com.codingame.rushhour

import java.lang.Math.abs

data class Coordinates(val x: Int, val y: Int) {
    fun next(direction: Direction): Coordinates {
        return when (direction) {
            Direction.UP -> Coordinates(x, y - 1)
            Direction.RIGHT -> Coordinates(x + 1, y)
            Direction.DOWN -> Coordinates(x, y + 1)
            Direction.LEFT -> Coordinates(x - 1, y)
        }
    }

    override fun toString(): String {
        return "$x;$y"
    }

    companion object {
        val MIN = Coordinates(0, 0)
        val MAX = Coordinates(5, 5)
    }

    val areValid by lazy { x in MIN.x..MAX.x && y in MIN.y..MAX.y }

    fun distanceFrom(other: Coordinates, axis: Axis): Int {
        return when (axis) {
            Axis.HORIZONTAL -> kotlin.math.abs(x - other.x)
            Axis.VERTICAL -> kotlin.math.abs(y - other.y)
        }
    }

}

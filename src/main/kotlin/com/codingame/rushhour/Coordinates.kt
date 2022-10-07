package com.codingame.rushhour

import java.lang.Math.abs

data class Coordinates(val x: Int, val y: Int) : Comparable<Coordinates> {
    fun next(direction: Direction): Coordinates {
        return when (direction) {
            Direction.UP -> Coordinates(x, y - 1)
            Direction.RIGHT -> Coordinates(x + 1, y)
            Direction.DOWN -> Coordinates(x, y + 1)
            Direction.LEFT -> Coordinates(x - 1, y)
        }
    }

    override fun compareTo(other: Coordinates): Int {
        return if (this == other) 0
        else if (x <= other.x && y <= other.y) -1
        else 1
    }

    override fun toString(): String {
        return "$x;$y"
    }

    companion object {
        val MIN = Coordinates(0, 0)
        val MAX = Coordinates(5, 5)
    }

    val areValid by lazy { this in MIN..MAX }

    fun distanceFrom(other: Coordinates, axis: Axis): Int {
        return when(axis) {
            Axis.HORIZONTAL -> kotlin.math.abs(x - other.x)
            Axis.VERTICAL -> kotlin.math.abs(y - other.y)
        }
    }

}

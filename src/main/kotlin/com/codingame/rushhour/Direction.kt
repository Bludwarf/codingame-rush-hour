package com.codingame.rushhour

enum class Direction {
    LEFT, RIGHT, UP, DOWN;

    fun isOn(axis: Axis): Boolean = when (this) {
        LEFT, RIGHT -> axis == Axis.HORIZONTAL
        UP, DOWN -> axis == Axis.VERTICAL
    }
}

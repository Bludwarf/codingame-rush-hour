package com.codingame.rushhour

enum class Axis {
    HORIZONTAL,
    VERTICAL;

    companion object {
        fun fromString(string: String): Axis = when(string) {
            "H" -> HORIZONTAL
            "V" -> VERTICAL
            else -> error("Unknown axis string $string")
        }
    }
}

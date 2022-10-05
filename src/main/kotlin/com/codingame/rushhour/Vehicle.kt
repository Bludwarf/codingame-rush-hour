package com.codingame.rushhour

class Vehicle(val id: Int, val length: Int, val axis: Axis) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vehicle) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return id.toString()
    }

    fun canGo(direction: Direction): Boolean = direction.isOn(axis)
    fun allCoordinatesFrom(minCoordinates: Coordinates): Sequence<Coordinates> {
        return sequence {
            yield(minCoordinates)

            var coordinates: Coordinates
            repeat(length - 1) {
                coordinates = when (axis) {
                    Axis.HORIZONTAL -> minCoordinates.next(Direction.RIGHT)
                    Axis.VERTICAL -> minCoordinates.next(Direction.DOWN)
                }
                yield(coordinates)
            }
        }
    }

    fun maxCoordinatesFrom(minCoordinates: Coordinates): Coordinates {
        return allCoordinatesFrom(minCoordinates).last()
    }
}

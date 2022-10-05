package com.codingame.rushhour

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VehicleTest {

    @Test
    fun maxCoordinatesFrom() {
        val vehicle = Vehicle(1, 2, Axis.VERTICAL)
        val minCoordinates = Coordinates(4,4)
        Assertions.assertThat(vehicle.maxCoordinatesFrom(minCoordinates)).isEqualTo(Coordinates(4,5))
    }
}

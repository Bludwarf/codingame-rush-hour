package com.codingame.rushhour

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class CoordinatesTest {

    @ParameterizedTest
    @CsvSource(
        "0,0,true",
        "5,5,true",
        "4,6,false",
        "6,6,false",
    )
    fun areValid(x: Int, y: Int, expected: Boolean) {
        val coordinates = Coordinates(x,y)
        Assertions.assertThat(coordinates.areValid).isEqualTo(expected)
    }
}

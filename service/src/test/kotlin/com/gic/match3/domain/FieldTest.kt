package com.gic.match3.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FieldTest {

    @Test
    fun `should initialize empty field with correct dimensions`() {
        val width = 5
        val height = 8
        val field = Field(width, height)

        assertEquals(width, field.width)
        assertEquals(height, field.height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                assertNull(field.get(x, y), "Cell at ($x, $y) should be empty")
            }
        }
    }

    @Test
    fun `should throw exception when x is negative`() {
        val field = Field(5, 5)
        val exception = assertThrows<IllegalArgumentException> { field.get(-1, 0) }
        assertTrue(exception.message!!.contains("out of bounds"))
    }

    @Test
    fun `should throw exception when y is negative`() {
        val field = Field(5, 5)
        val exception = assertThrows<IllegalArgumentException> { field.get(0, -1) }
        assertTrue(exception.message!!.contains("out of bounds"))
    }

    @Test
    fun `should throw exception when x exceeds width`() {
        val field = Field(5, 5)
        val exception = assertThrows<IllegalArgumentException> { field.get(5, 0) }
        assertTrue(exception.message!!.contains("out of bounds"))
    }

    @Test
    fun `should throw exception when y exceeds height`() {
        val field = Field(5, 5)
        val exception = assertThrows<IllegalArgumentException> { field.get(0, 5) }
        assertTrue(exception.message!!.contains("out of bounds"))
    }
}
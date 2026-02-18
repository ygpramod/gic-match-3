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

    @Test
    fun `should detect and remove horizontal match of 3`() {
        val field = Field(5, 5)
        // Row 4: [*, *, *, @, ^]
        field.set(0, 4, Symbol.Star)
        field.set(1, 4, Symbol.Star)
        field.set(2, 4, Symbol.Star) // The "new" piece
        field.set(3, 4, Symbol.At)

        // FIX: Pass the coordinate of the piece that completed the match (e.g., (2,4))
        val removed = field.removeMatches(listOf(2 to 4))

        assertEquals(3, removed)
        assertNull(field.get(0, 4))
        assertNull(field.get(1, 4))
        assertNull(field.get(2, 4))
        assertEquals(Symbol.At, field.get(3, 4))
    }

    @Test
    fun `should detect and remove vertical match of 3`() {
        val field = Field(5, 5)
        // Column 2: [*, *, *, @, ^]
        field.set(2, 0, Symbol.Star)
        field.set(2, 1, Symbol.Star)
        field.set(2, 2, Symbol.Star) // The "new" piece
        field.set(2, 3, Symbol.At)

        // FIX: Pass the coordinate of the piece that completed the match
        val removed = field.removeMatches(listOf(2 to 2))

        assertEquals(3, removed)
        assertNull(field.get(2, 0))
        assertNull(field.get(2, 1))
        assertNull(field.get(2, 2))
        assertEquals(Symbol.At, field.get(2, 3))
    }

    @Test
    fun `should detect match using targeted scan`() {
        val field = Field(5, 5)
        // Row 4 is almost full: [*, *, (empty), @, ^]
        field.set(0, 4, Symbol.Star)
        field.set(1, 4, Symbol.Star)

        // We place the 3rd Star at (2, 4)
        field.set(2, 4, Symbol.Star)
        val recentlyPlaced = listOf(2 to 4)

        // Act: Scan only relevant cells
        val removed = field.removeMatches(recentlyPlaced)

        // Assert: It scanned Row 4 successfully
        assertEquals(3, removed)
        assertNull(field.get(2, 4))
    }
}
package com.gic.match3.parser

import com.gic.match3.domain.Orientation
import com.gic.match3.domain.Symbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InputParserTest {

    @Test
    fun `should parse valid dimensions`() {
        val input = "5 8"
        val config = InputParser.parse(input)

        assertEquals(5, config.width)
        assertEquals(8, config.height)
        assertEquals(0, config.bricks.size)
    }

    @Test
    fun `should throw exception for non-numeric dimensions`() {
        val exception = assertThrows<IllegalArgumentException> {
            InputParser.parse("A B")
        }
        assertEquals("Dimensions must be valid integers", exception.message)
    }

    @Test
    fun `should throw exception for non-positive dimensions`() {
        val exception = assertThrows<IllegalArgumentException> {
            InputParser.parse("0 5")
        }
        assertEquals("Dimensions must be positive", exception.message)
    }

    @Test
    fun `should parse a single horizontal brick`() {
        val input = "5 5 H^^*"
        val config = InputParser.parse(input)

        assertEquals(1, config.bricks.size)
        val brick = config.bricks[0]

        assertEquals(Orientation.Horizontal, brick.orientation)
        assertEquals(listOf(Symbol.Triangle, Symbol.Triangle, Symbol.Star), brick.symbols)
    }

    @Test
    fun `should throw exception for invalid orientation`() {
        val exception = assertThrows<IllegalArgumentException> {
            InputParser.parse("5 5 X^^^")
        }
        assertTrue(exception.message!!.contains("Invalid orientation"))
    }

    @Test
    fun `should throw exception for invalid symbol`() {
        val exception = assertThrows<IllegalArgumentException> {
            InputParser.parse("5 5 H^a^")
        }
        assertTrue(exception.message!!.contains("Invalid symbol"))
    }

    @Test
    fun `should throw exception for more than 5 bricks`() {
        val input = "5 5 H^^^ H^^^ H^^^ H^^^ H^^^ H^^^"

        val exception = assertThrows<IllegalArgumentException> {
            InputParser.parse(input)
        }

        assertEquals("Maximum 5 bricks allowed", exception.message)
    }
}
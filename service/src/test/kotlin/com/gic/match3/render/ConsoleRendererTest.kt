package com.gic.match3.render

import com.gic.match3.domain.ActiveBrick
import com.gic.match3.domain.Brick
import com.gic.match3.domain.Field
import com.gic.match3.domain.Orientation
import com.gic.match3.domain.Symbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConsoleRendererTest {

    @Test
    fun `should render empty field using dots`() {
        val field = Field(3, 3)
        val output = ConsoleRenderer.render(field, null)
        val expected = """
            . . .
            . . .
            . . .
        """.trimIndent()
        assertEquals(expected, output)
    }

    @Test
    fun `should render field with stationary symbols`() {
        val field = Field(3, 3)
        field.set(0, 0, Symbol.Triangle) // Top-Left (^)
        field.set(1, 1, Symbol.Star)     // Center (*)

        // Note: Renderer adds spaces between columns
        val expected = """
            ^ . .
            . * .
            . . .
        """.trimIndent()

        assertEquals(expected, ConsoleRenderer.render(field, null))
    }

    @Test
    fun `should render active brick overlay`() {
        val field = Field(5, 5)
        field.set(0, 4, Symbol.At) // @ at bottom-left (stationary)

        val brickData = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        // Active brick at (2, 1) -> occupies (2,1), (2,2), (2,3)
        val activeBrick = ActiveBrick(brickData, 2, 1)

        val output = ConsoleRenderer.render(field, activeBrick)

        val expected = """
            . . . . .
            . . * . .
            . . * . .
            . . * . .
            @ . . . .
        """.trimIndent()
        assertEquals(expected, output)
    }
}
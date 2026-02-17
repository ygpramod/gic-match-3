package com.gic.match3.render

import com.gic.match3.domain.Field
import com.gic.match3.domain.Symbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConsoleRendererTest {

    @Test
    fun `should render empty field using dots`() {
        val field = Field(3, 3)
        val output = ConsoleRenderer.render(field)
        val expected = """
            ...
            ...
            ...
        """.trimIndent()
        assertEquals(expected, output)
    }

    @Test
    fun `should render field with symbols`() {
        val field = Field(3, 3)

        // Set symbols at specific positions
        field.set(0, 0, Symbol.Triangle) // Top-Left (^)
        field.set(1, 1, Symbol.Star)     // Center (*)

        val expected = """
            ^..
            .*.
            ...
        """.trimIndent()

        assertEquals(expected, ConsoleRenderer.render(field))
    }
}
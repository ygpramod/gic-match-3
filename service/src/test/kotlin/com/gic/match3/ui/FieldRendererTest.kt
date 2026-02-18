package com.gic.match3.ui

import com.gic.match3.domain.ActiveBrick
import com.gic.match3.domain.Brick
import com.gic.match3.domain.Field
import com.gic.match3.domain.Orientation
import com.gic.match3.domain.Symbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FieldRendererTest {

    @Test
    fun `should render completely empty field`() {
        val field = Field(2, 2)
        val renderer = FieldRenderer()
        val output = renderer.render(field, null)
        val expected = """
            . .
            . .
        """.trimIndent()
        assertEquals(expected, output)
    }

    @Test
    fun `should render field state as string`() {
        val field = Field(3, 3)
        field.set(0, 0, Symbol.Star)
        field.set(2, 2, Symbol.At)
        val renderer = FieldRenderer()
        val output = renderer.render(field)
        val expected = """
            * . .
            . . .
            . . @
        """.trimIndent()
        assertEquals(expected, output)
    }

    @Test
    fun `should render field with active brick overlay`() {
        val field = Field(5, 5)
        field.set(0, 4, Symbol.At) // @ at bottom-left
        val brickData = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val activeBrick = ActiveBrick(brickData, 2, 1)
        val renderer = FieldRenderer()
        val output = renderer.render(field, activeBrick)
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
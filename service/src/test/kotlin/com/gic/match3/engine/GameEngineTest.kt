package com.gic.match3.engine

import com.gic.match3.domain.Brick
import com.gic.match3.domain.Command
import com.gic.match3.domain.GameConfig
import com.gic.match3.domain.GameStatus
import com.gic.match3.domain.Orientation
import com.gic.match3.domain.Symbol
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GameEngineTest {

    @Test
    fun `should initialize game with correct field dimensions`() {
        val config = GameConfig(10, 20, emptyList())
        val engine = GameEngine(config)
        assertEquals(10, engine.field.width)
        assertEquals(20, engine.field.height)
    }

    @Test
    fun `should render current field state`() {
        val config = GameConfig(3, 3, emptyList())
        val engine = GameEngine(config)
        val output = engine.render()
        val expected = """
            . . .
            . . .
            . . .
        """.trimIndent()
        assertEquals(expected, output)
    }

    @Test
    fun `should spawn vertical brick centered`() {
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        val active = engine.activeBrick!!

        assertEquals(brick, active.brick)
        assertEquals(Orientation.Vertical, active.brick.orientation)
        assertEquals(2, active.x)
        assertEquals(0, active.y)
    }

    @Test
    fun `should handle even width field correctly`() {
        val brick = Brick(Orientation.Horizontal, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(6, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        assertEquals(1, engine.activeBrick!!.x)
    }

    @Test
    fun `should not spawn if no bricks remain`() {
        val config = GameConfig(5, 10, emptyList())
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        assertNull(engine.activeBrick)
    }

    @Test
    fun `should spawn horizontal brick at centered position`() {
        val brick = Brick(Orientation.Horizontal, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)
        assertNull(engine.activeBrick)

        engine.spawnNextBrick()
        assertNotNull(engine.activeBrick)

        val active = engine.activeBrick!!
        assertEquals(brick, active.brick)
        assertEquals(0, active.y, "Should spawn at Row 0")
        assertEquals(1, active.x, "Should be centered (Field 5 - Brick 3) / 2 = 1")
    }

    @Test
    fun `should move active brick down on tick`() {
        val brick = Brick(Orientation.Horizontal, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        val initialY = engine.activeBrick!!.y // Should be 0

        engine.tick()

        val newY = engine.activeBrick!!.y
        assertEquals(initialY + 1, newY, "Brick should have moved down by 1 row")
    }

    @Test
    fun `should lock horizontal brick into field when hitting bottom`() {
        val brick = Brick(Orientation.Horizontal, listOf(Symbol.Star, Symbol.At, Symbol.Star))
        val config = GameConfig(5, 5, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        engine.activeBrick!!.y = 4

        engine.tick()

        assertNull(engine.activeBrick, "Active brick should be null after locking")

        assertEquals(Symbol.Star, engine.field.get(1, 4))
        assertEquals(Symbol.At, engine.field.get(2, 4))
        assertEquals(Symbol.Star, engine.field.get(3, 4))
    }

    @Test
    fun `should lock vertical brick into field when hitting bottom`() {
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Triangle, Symbol.At, Symbol.Star))
        val config = GameConfig(5, 5, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        engine.activeBrick!!.y = 2

        engine.tick()

        assertNull(engine.activeBrick, "Active brick should be null after locking")
        assertEquals(Symbol.Triangle, engine.field.get(2, 2))
        assertEquals(Symbol.At, engine.field.get(2, 3))
        assertEquals(Symbol.Star, engine.field.get(2, 4))
    }

    @Test
    fun `should stack horizontal bricks`() {
        val hBrick = Brick(Orientation.Horizontal, listOf(Symbol.Star, Symbol.Star, Symbol.At))
        val config = GameConfig(5, 5, listOf(hBrick, hBrick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        engine.activeBrick!!.y = 4
        engine.tick()

        engine.spawnNextBrick()
        val active = engine.activeBrick!!

        active.y = 3

        engine.tick()

        assertNull(engine.activeBrick)
        assertEquals(Symbol.Star, engine.field.get(1, 3))
    }

    @Test
    fun `should stack vertical bricks`() {
        val vBrick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Tilde, Symbol.At))
        val config = GameConfig(5, 6, listOf(vBrick, vBrick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        engine.activeBrick!!.y = 3
        engine.tick() // Locks

        engine.spawnNextBrick()
        assertEquals(0, engine.activeBrick!!.y)

        engine.tick()

        assertNull(engine.activeBrick, "Brick should lock when hitting the stack")

        assertEquals(Symbol.At, engine.field.get(2, 2)) // Bottom of top brick
        assertEquals(Symbol.Star, engine.field.get(2, 3)) // Top of bottom brick
    }

    @Test
    fun `should stack horizontal brick on top of vertical brick`() {
        val vBrick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.At))
        val hBrick = Brick(Orientation.Horizontal, listOf(Symbol.Tilde, Symbol.At, Symbol.At))

        val config = GameConfig(5, 6, listOf(vBrick, hBrick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        engine.activeBrick!!.y = 3
        engine.tick() // Locks

        engine.spawnNextBrick()

        engine.activeBrick!!.y = 2

        engine.tick()

        assertNull(engine.activeBrick, "Horizontal brick should lock on top of vertical")
        assertEquals(Symbol.At, engine.field.get(2, 2)) // Center of Horizontal
        assertEquals(Symbol.Star, engine.field.get(2, 3)) // Top of Vertical
    }

    @Test
    fun `should stack vertical brick on top of horizontal brick`() {
        val hBrick = Brick(Orientation.Horizontal, listOf(Symbol.Tilde, Symbol.At, Symbol.At))
        val vBrick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.At, Symbol.Star))

        val config = GameConfig(5, 6, listOf(hBrick, vBrick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        engine.activeBrick!!.y = 5
        engine.tick() // Locks

        engine.spawnNextBrick()

        engine.activeBrick!!.y = 2

        engine.tick()

        assertNull(engine.activeBrick, "Vertical brick should lock on top of horizontal")
        assertEquals(Symbol.Star, engine.field.get(2, 4)) // Bottom of Vertical (Y=2+2)
        assertEquals(Symbol.At, engine.field.get(2, 5))   // Center of Horizontal
    }

    @Test
    fun `should move active brick left`() {
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        engine.input(Command.Left)
        assertEquals(1, engine.activeBrick!!.x)
    }

    @Test
    fun `should move active brick right`() {
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()

        engine.input(Command.Right)
        assertEquals(3, engine.activeBrick!!.x)
    }

    @Test
    fun `should not move past left wall`() {
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        engine.activeBrick!!.x = 0 // Force to left edge

        engine.input(Command.Left)
        assertEquals(0, engine.activeBrick!!.x)
    }

    @Test
    fun `should not move past right wall`() {
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        engine.activeBrick!!.x = 4 // Force to right edge

        engine.input(Command.Right)
        assertEquals(4, engine.activeBrick!!.x)
    }

    @Test
    fun `should not move right into an existing brick`() {
        //
        val vBrick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.At, Symbol.Star))
        val config = GameConfig(5, 5, listOf(vBrick, vBrick))
        val engine = GameEngine(config)

        // 1. Drop first brick in Column 1
        engine.spawnNextBrick()
        // Force to Column 1, Bottom (y=2 -> occupies 2,3,4)
        engine.activeBrick!!.x = 1
        engine.activeBrick!!.y = 2
        engine.tick() // Locks

        // 2. Spawn second brick in Column 0
        engine.spawnNextBrick()
        engine.activeBrick!!.x = 0
        // Move it down to y=2 (side-by-side with the obstacle)
        engine.activeBrick!!.y = 2

        // 3. Try to move Right (into the obstacle)
        engine.input(Command.Right)

        // 4. Verify it stayed at x=0
        assertEquals(0, engine.activeBrick!!.x, "Should not move into occupied space")
    }

    @Test
    fun `should hard drop brick to the bottom`() {
        //
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.At, Symbol.Star))
        // Field 5x10
        val config = GameConfig(5, 10, listOf(brick))
        val engine = GameEngine(config)

        engine.spawnNextBrick()
        // Starts at y=0.

        // Act: Hard Drop
        engine.input(Command.Down)

        // Assert: Brick is locked (null)
        assertNull(engine.activeBrick, "Brick should be locked after hard drop")

        // Assert: Field has the brick at the bottom
        // Vertical brick height 3. Bottom is at index 9.
        // Occupies: 7, 8, 9.
        assertEquals(Symbol.Star, engine.field.get(2, 9)) // Bottom
        assertEquals(Symbol.At, engine.field.get(2, 8)) // Middle
        assertEquals(Symbol.Star, engine.field.get(2, 7)) // Top
    }

    @Test
    fun `should set game over when spawn area is blocked`() {
        //
        val brick = Brick(Orientation.Vertical, listOf(Symbol.Star, Symbol.Star, Symbol.Star))
        val config = GameConfig(5, 5, listOf(brick))
        val engine = GameEngine(config)

        // 1. Block the spawn point (Center X=2, Y=0)
        // Note: Field width 5. Brick width 1. Start X = (5-1)/2 = 2.
        engine.field.set(2, 0, Symbol.At)

        // 2. Try to spawn
        engine.spawnNextBrick()

        // 3. Assert Game Over
        assertEquals(GameStatus.GAME_OVER, engine.status)
        assertNull(engine.activeBrick, "Should not spawn brick if blocked")
    }
}
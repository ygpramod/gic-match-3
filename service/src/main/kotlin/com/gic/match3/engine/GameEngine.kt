package com.gic.match3.engine

import com.gic.match3.domain.ActiveBrick
import com.gic.match3.domain.Brick
import com.gic.match3.domain.Field
import com.gic.match3.domain.GameConfig
import com.gic.match3.domain.GameStatus
import com.gic.match3.domain.Orientation
import com.gic.match3.render.ConsoleRenderer
import java.util.LinkedList
import java.util.Queue

class GameEngine(config: GameConfig) {
    val field: Field = Field(config.width, config.height)
    private val brickQueue: Queue<Brick> = LinkedList(config.bricks)

    var status: GameStatus = GameStatus.RUNNING
        private set

    var activeBrick: ActiveBrick? = null
        private set

    fun spawnNextBrick() {
        val nextBrick = brickQueue.poll() ?: return // TODO: Handle Game End if empty?

        val brickWidth = if (nextBrick.orientation == Orientation.Horizontal) 3 else 1
        val startX = (field.width - brickWidth) / 2
        val startY = 0

        activeBrick = ActiveBrick(nextBrick, startX, startY)
    }

    fun tick() {
        val active = activeBrick ?: return
        val nextY = active.y + 1

        if (canMoveTo(active, nextY)) {
            active.y = nextY
        } else {
            lockBrick(active)
        }
    }

    private fun canMoveTo(active: ActiveBrick, targetY: Int): Boolean {
        val heightOffset = if (active.brick.orientation == Orientation.Vertical) 2 else 0
        if (targetY + heightOffset >= field.height) return false

        val simulatedBrick = active.copy(y = targetY)

        return simulatedBrick.occupiedCells()
            .none { (x, y, _) -> field.isOccupied(x, y) }
    }

    private fun lockBrick(active: ActiveBrick) {
        active.occupiedCells().forEach { (x, y, symbol) ->
            field.set(x, y, symbol)
        }
        activeBrick = null
    }

    fun endGame() {
        status = GameStatus.GAME_OVER
    }

    fun render(): String {
        return ConsoleRenderer.render(field)
    }
}
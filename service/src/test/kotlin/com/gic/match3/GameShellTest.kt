package com.gic.match3.engine

import com.gic.match3.domain.ActiveBrick
import com.gic.match3.domain.Brick
import com.gic.match3.domain.Command
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
        if (status == GameStatus.GAME_OVER) return

        val nextBrick = brickQueue.poll()
        if (nextBrick == null) {
            status = GameStatus.GAME_OVER
            return
        }

        val brickWidth = if (nextBrick.orientation == Orientation.Horizontal) 3 else 1
        val startX = (field.width - brickWidth) / 2
        val startY = 0

        val tempBrick = ActiveBrick(nextBrick, startX, startY)

        if (field.canFit(tempBrick, startX, startY)) activeBrick = tempBrick
        else status = GameStatus.GAME_OVER
    }

    fun tick() {
        val active = activeBrick ?: return
        if (!tryMove(active, 0, 1)) lockBrick(active)
    }

    fun input(command: Command) {
        val active = activeBrick ?: return

        when (command) {
            Command.Left -> tryMove(active, -1, 0)
            Command.Right -> tryMove(active, 1, 0)
            Command.Down -> performHardDrop(active)
        }
    }

    private fun performHardDrop(active: ActiveBrick) {
        var targetY = active.y
        while (field.canFit(active, active.x, targetY + 1)) {
            targetY++
        }
        active.y = targetY
        lockBrick(active)
    }

    private fun tryMove(active: ActiveBrick, dx: Int, dy: Int): Boolean {
        val nextX = active.x + dx
        val nextY = active.y + dy

        if (field.canFit(active, nextX, nextY)) {
            active.x = nextX
            active.y = nextY
            return true
        }
        return false
    }

    private fun lockBrick(active: ActiveBrick) {
        field.place(active)
        field.removeMatches(active.occupiedCells().map { (x, y, _) -> x to y })
        activeBrick = null
    }

    fun endGame() {
        status = GameStatus.GAME_OVER
    }

    fun render(): String {
        return ConsoleRenderer.render(field, activeBrick)
    }
}
package com.gic.match3.domain

class Field(val width: Int, val height: Int) {
    private val grid = Array(height) { Array<Symbol?>(width) { null } }

    fun get(x: Int, y: Int): Symbol? {
        require(isValid(x, y)) { "Coordinate ($x, $y) out of bounds" }
        return grid[y][x]
    }

    fun set(x: Int, y: Int, symbol: Symbol?) {
        require(isValid(x, y)) { "Coordinate ($x, $y) out of bounds" }
        grid[y][x] = symbol
    }

    private fun isValid(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    fun isOccupied(x: Int, y: Int): Boolean {
        return isValid(x, y) && grid[y][x] != null
    }

    fun canFit(brick: ActiveBrick, targetX: Int, targetY: Int): Boolean {
        val simulated = brick.copy(x = targetX, y = targetY)

        return simulated.occupiedCells().all { (x, y, _) ->
            val inBounds = isValid(x, y)
            inBounds && !isOccupied(x, y)
        }
    }

    fun place(brick: ActiveBrick) {
        brick.occupiedCells().forEach { (x, y, symbol) ->
            set(x, y, symbol)
        }
    }
}
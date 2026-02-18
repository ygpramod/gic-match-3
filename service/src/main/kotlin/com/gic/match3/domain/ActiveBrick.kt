package com.gic.match3.domain

data class ActiveBrick(
    val brick: Brick,
    var x: Int,
    var y: Int
) {
    /**
     * Returns a list of (x, y, symbol) tuples representing the brick's current position on the grid.
     */
    fun occupiedCells(): List<Triple<Int, Int, Symbol>> {
        val s = brick.symbols
        return if (brick.orientation == Orientation.Horizontal) {
            listOf(
                Triple(x, y, s[0]),
                Triple(x + 1, y, s[1]),
                Triple(x + 2, y, s[2])
            )
        } else {
            listOf(
                Triple(x, y, s[0]),
                Triple(x, y + 1, s[1]),
                Triple(x, y + 2, s[2])
            )
        }
    }
}
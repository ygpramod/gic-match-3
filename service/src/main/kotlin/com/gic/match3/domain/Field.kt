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

    /**
     * Checks for and removes matches only in the rows and columns
     * intersecting with the provided cells.
     */
    fun removeMatches(cellsToCheck: List<Pair<Int, Int>>): Int {
        val toRemove = mutableSetOf<Pair<Int, Int>>()

        // 1. Scan Horizontal Rows involved
        val rowsToScan = cellsToCheck.map { it.second }.distinct()
        for (y in rowsToScan) {
            if (y in 0 until height) {
                collectMatchesInLine(width, toRemove) { x -> x to y }
            }
        }

        // 2. Scan Vertical Columns involved
        val colsToScan = cellsToCheck.map { it.first }.distinct()
        for (x in colsToScan) {
            if (x in 0 until width) {
                collectMatchesInLine(height, toRemove) { y -> x to y }
            }
        }

        // 3. Remove
        toRemove.forEach { (x, y) ->
            grid[y][x] = null
        }
        return toRemove.size
    }

    private fun collectMatchesInLine(
        length: Int,
        toRemove: MutableSet<Pair<Int, Int>>,
        coordinateMapper: (Int) -> Pair<Int, Int>
    ) {
        var matchCount = 1
        // iterate from 0 to length-2 to check pairs (i, i+1)
        for (i in 0 until length - 1) {
            val (currentX, currentY) = coordinateMapper(i)
            val currentSymbol = get(currentX, currentY)

            val (nextX, nextY) = coordinateMapper(i + 1)
            val nextSymbol = get(nextX, nextY)

            if (currentSymbol != null && currentSymbol == nextSymbol) {
                matchCount++
            } else {
                if (matchCount >= 3) {
                    for (k in 0 until matchCount) {
                        toRemove.add(coordinateMapper(i - k))
                    }
                }
                matchCount = 1
            }
        }
        // Check the last sequence at the end of the line
        if (matchCount >= 3) {
            for (k in 0 until matchCount) {
                toRemove.add(coordinateMapper(length - 1 - k))
            }
        }
    }
}
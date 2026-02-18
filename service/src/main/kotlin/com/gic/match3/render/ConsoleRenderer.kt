package com.gic.match3.render

import com.gic.match3.domain.ActiveBrick
import com.gic.match3.domain.Field

object ConsoleRenderer {

    fun render(field: Field, activeBrick: ActiveBrick? = null): String {
        // 1. Create a buffer initialized with Field data
        val buffer = Array(field.height) { y ->
            Array(field.width) { x ->
                field.get(x, y)?.toString() ?: "."
            }
        }

        // 2. Overlay Active Brick (if it exists)
        activeBrick?.let { brick ->
            brick.occupiedCells().forEach { (x, y, symbol) ->
                // Only draw if within bounds (ignore parts that might be above the ceiling)
                if (x in 0 until field.width && y in 0 until field.height) {
                    buffer[y][x] = symbol.toString()
                }
            }
        }

        // 3. Convert Buffer to String
        val sb = StringBuilder()
        for (y in 0 until field.height) {
            for (x in 0 until field.width) {
                sb.append(buffer[y][x])
                // Add space between columns, but not after the last one
                if (x < field.width - 1) {
                    sb.append(" ")
                }
            }
            // Add newline between rows, but not after the last one
            if (y < field.height - 1) {
                sb.append("\n")
            }
        }
        return sb.toString()
    }
}
package com.gic.match3.parser

import com.gic.match3.domain.Brick
import com.gic.match3.domain.GameConfig
import com.gic.match3.domain.Orientation
import com.gic.match3.domain.Symbol

object InputParser {

    fun parse(input: String): GameConfig {
        val parts = input.trim().split("\\s+".toRegex())

        require(parts.size >= 2) { "Input must contain at least width and height" }

        val width = parts[0].toIntOrNull()
        val height = parts[1].toIntOrNull()

        require(width != null && height != null) { "Dimensions must be valid integers" }
        require(width > 0 && height > 0) { "Dimensions must be positive" }

        val brickTokens = parts.drop(2)
        require(brickTokens.size <= 5) { "Maximum 5 bricks allowed" }

        val bricks = brickTokens.map { parseBrick(it) }

        return GameConfig(width, height, bricks)
    }

    private fun parseBrick(token: String): Brick {
        require(token.length == 4) { "Invalid brick format: $token" }

        val orientation = when (val orientationChar = token[0].uppercaseChar()) {
            'H' -> Orientation.Horizontal
            'V' -> Orientation.Vertical
            else -> throw IllegalArgumentException("Invalid orientation: $orientationChar")
        }

        val symbols = token.substring(1).map { char ->
            Symbol.values().find { it.char == char }
                ?: throw IllegalArgumentException("Invalid symbol: $char")
        }

        return Brick(orientation, symbols)
    }
}
package com.gic.match3.domain

data class Brick(
    val orientation: Orientation,
    val symbols: List<Symbol>
) {
    init {
        require(symbols.size == 3) { "A brick must contain exactly 3 symbols" }
    }
}

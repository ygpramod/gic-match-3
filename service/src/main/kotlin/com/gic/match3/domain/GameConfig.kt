package com.gic.match3.domain

data class GameConfig(
    val width: Int,
    val height: Int,
    val bricks: List<Brick>
)

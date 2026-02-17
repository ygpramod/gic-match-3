package com.gic.match3.render

import com.gic.match3.domain.Field

object ConsoleRenderer {

    fun render(field: Field): String {
        val sb = StringBuilder()

        for (y in 0 until field.height) {
            for (x in 0 until field.width) {
                val symbol = field.get(x, y)
                if (symbol == null) sb.append('.')
                else sb.append(symbol.char)
            }
            if (y < field.height - 1) sb.append('\n')
        }
        return sb.toString()
    }
}
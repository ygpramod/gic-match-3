package com.gic.match3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class GameShellTest {

    @Test
    fun `should handle full game lifecycle`() {
        val inputSequence = """
            BAD_INPUT
            5 5 V***
            L
            
            
            
            Q
        """.trimIndent()

        val inputStream = ByteArrayInputStream(inputSequence.toByteArray(StandardCharsets.UTF_8))
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)

        val shell = GameShell(inputStream, printStream)
        shell.run()

        val output = outputStream.toString(StandardCharsets.UTF_8)

        // Verifications
        assertTrue(output.contains("Match-3 Game Shell"), "Should show startup banner")
        assertTrue(output.contains("Error:"), "Should display error for bad input")
        assertTrue(output.contains("Init>"), "Should show Init prompt")
        assertTrue(output.contains("Game>"), "Should show Game prompt")
        assertTrue(output.contains("Game Over"), "Should eventually reach Game Over")
    }
}
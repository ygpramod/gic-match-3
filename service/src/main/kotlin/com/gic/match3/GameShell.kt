package com.gic.match3

import com.gic.match3.domain.Command
import com.gic.match3.domain.GameStatus
import com.gic.match3.engine.GameEngine
import com.gic.match3.parser.InputParser
import java.io.InputStream
import java.io.PrintStream
import java.util.Scanner

class GameShell(
    input: InputStream,
    private val output: PrintStream
) {
    private val scanner = Scanner(input)

    fun run() {
        while (true) {
            val engine = initializeGame()
            runGameLoop(engine)
            if (handleGameEnd()) continue
            else break
        }
    }

    private fun initializeGame(): GameEngine {
        while (true) {
            output.println("Please enter field size (width and height) and up to 5 bricks set:")
            if (!scanner.hasNextLine()) throw RuntimeException("Input stream closed unexpectedly")

            val line = scanner.nextLine()
            try {
                val config = InputParser.parse(line)
                val engine = GameEngine(config)
                engine.spawnNextBrick()
                return engine
            } catch (e: Exception) {
                output.println("Error: ${e.message}")
            }
        }
    }

    private fun runGameLoop(engine: GameEngine) {
        while (engine.status == GameStatus.RUNNING) {
            output.println(engine.render())
            output.println("Enter up to 2 commands to process before moving to the next frame (valid commands are L,R,D)")
            if (!scanner.hasNextLine()) break
            val line = scanner.nextLine()
            processCommands(line, engine)
            engine.tick()
            if (engine.activeBrick == null && engine.status == GameStatus.RUNNING) {
                engine.spawnNextBrick()
            }
        }
        output.println(engine.render())
    }

    private fun processCommands(line: String, engine: GameEngine) {
        val validCommands = line.mapNotNull { char ->
            when (char.uppercaseChar()) {
                'L' -> Command.Left
                'R' -> Command.Right
                'D' -> Command.Down
                else -> null
            }
        }
        validCommands.take(2).forEach { command ->
            engine.input(command)
        }
    }

    private fun handleGameEnd(): Boolean {
        output.println("Game Over")
        while (true) {
            output.println("Enter S to start over or Q to quit")
            if (!scanner.hasNextLine()) return false

            val input = scanner.nextLine().trim().uppercase()
            when (input) {
                "S" -> return true
                "Q" -> {
                    output.println("Thank you for playing Match-3!")
                    return false
                }
                else -> {
                    // Invalid input, re-prompt (implied by loop)
                }
            }
        }
    }
}
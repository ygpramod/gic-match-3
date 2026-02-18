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
        output.println("Match-3 Game Shell v1.2 - Ready.")
        output.flush()

        while (true) {
            try {
                val engine = initializeGame()
                runGameLoop(engine)
                if (handleGameEnd()) continue
                else break // Quit (Q)
            } catch (e: Exception) {
                output.println("Critical Error in main loop: ${e.message}")
                output.flush()
                break
            }
        }
    }

    private fun initializeGame(): GameEngine {
        while (true) {
            output.println("Please enter field size (width and height) and up to 5 bricks:")
            output.print("Init> ")
            output.flush()

            if (!scanner.hasNextLine()) throw RuntimeException("Input stream closed unexpectedly")

            val line = scanner.nextLine()
            try {
                if (line.isBlank()) continue
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
            output.println("Enter commands (L,R,D):")
            output.print("Game> ")
            output.flush()
            if (!scanner.hasNextLine()) break
            val line = scanner.nextLine()
            processCommands(line, engine)
            engine.tick()
            if (engine.activeBrick == null && engine.status == GameStatus.RUNNING) {
                engine.spawnNextBrick()
            }
        }
        output.println(engine.render())
        output.flush()
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
        output.flush()

        while (true) {
            output.print("Enter S to start over or Q to quit: ")
            output.flush()
            if (!scanner.hasNextLine()) return false

            val input = scanner.nextLine().trim().uppercase()
            when (input) {
                "S" -> return true
                "Q" -> {
                    output.println("Thank you for playing Match-3!")
                    output.flush()
                    return false
                }
                else -> output.println("Invalid option.")
            }
        }
    }
}
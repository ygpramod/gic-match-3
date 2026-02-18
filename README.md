# GIC Match-3 Falling Bricks

A console-based implementation of a Match-3 game with falling bricks, written in Kotlin. This project demonstrates strict adherence to a requirements specification (RFC), utilizing Domain-Driven Design (DDD) principles and Test-Driven Development (TDD).

## 📋 Features

* **Custom Game Engine:** Handles gravity, collision detection, and object locking.
* **Match-3 Mechanics:** Detects and removes horizontal/vertical matches of 3+ identical symbols.
* **Input Parsing:** Validates strict input formats for board configuration and brick sequences.
* **Interactive Shell:** A CLI loop that processes user commands and renders the game state.
* **RFC Compliant:** Adheres strictly to the provided requirements (see `docs/rfc/requirements.md`).

## 🛠️ Prerequisites

* JDK 17 or higher
* Kotlin 2.3.10 (handled via Gradle wrapper)

## 🚀 How to Build & Test

The project uses Gradle for build automation.

### Build the Project
```bash
./gradlew clean build
```
Run Unit & Integration Tests
```bash
./gradlew test
```

## 🎮 How to Play

### Start the Game

    ./gradlew :service:run --console=plain -q

Note: The --console=plain flag is recommended to prevent Gradle's progress bars from interfering with the game board rendering.

### Initialization
    
    When prompted, enter the field size and a list of bricks.

    Format: <width> <height> <brick1> <brick2> ...

    Width/Height: Integers > 0

    Bricks: Up to 5 bricks defined by orientation (H or V) and 3 symbols.

    Example Input: 5 6 H^^^ V*@*

### 🎮 Controls

| Command | Action |
| :--- | :--- |
| L | Move active brick **Left** |
| R | Move active brick **Right** |
| D | **Hard Drop** (Move brick to bottom instantly) |

### Example Turn:
    LD
(Moves left once, then drops immediately)

### Game Rules

- Gravity: Bricks fall 1 unit per turn automatically after commands are processed.
- Matching: 3 or more identical symbols (horizontally or vertically) disappear.
- Game Over: Occurs if a new brick cannot spawn (not enough space) or if the brick queue is exhausted.****

## 📂 Project Structure

    ├── docs/               # Requirements and implementation plans
    ├── service/
    │   ├── src/main/       # Source code (Domain, Engine, Parser, Render)
    │   └── src/test/       # Unit and Integration tests
    ├── build.gradle.kts    # Build configuration
    └── settings.gradle.kts # Project settings
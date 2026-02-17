# Implementation Plan: Vertical Slices

This document outlines the step-by-step implementation plan for the Match-3 Falling Bricks Game.
Each slice represents a shippable, testable increment of functionality.

## Slice 1: Skeleton & Build Fix
**Goal:** Establish a working build environment.
- Fix `build.gradle.kts` syntax errors.
- Configure JUnit 5.
- Create empty `Main.kt`.
- **Exit Criteria:** `gradle build` passes; "Hello World" prints to console.

## Slice 2: Domain Primitives & Input Parsing
**Goal:** Model the core data structures and parse user input.
- Implement `Field` (grid), `Brick` (shape/symbol), `Coordinate`.
- Implement `InputParser` to handle `<width> <height> <bricks...>` format.
- **Tests:** Unit tests for valid/invalid input strings and coordinate mapping.

## Slice 3: Game Loop & Rendering
**Goal:** Create the engine heartbeat and visual output.
- Implement `GameEngine` loop structure (Frame-based).
- Implement `ConsoleRenderer` (ASCII output).
- Handle Game Initialization prompt.
- Handle Game End (S/Q) logic.
- **Tests:** Integration test: Start game -> Print empty board -> Quit.

## Slice 4: Spawning & Gravity
**Goal:** Bricks appear and fall automatically.
- Implement `Spawner` (instantiate bricks from list).
- Implement `GravitySystem` (automatic drop per frame).
- **Tests:** Brick spawns at top; Brick Y-coordinate increases over frames.

## Slice 5: Commands & Collision
**Goal:** User control and physics.
- Implement `CommandProcessor` (L, R, D inputs).
- Implement `CollisionDetector` (Walls, Floor, Other Bricks).
- Implement Locking (Brick becomes stationary).
- **Tests:** Move left/right; Wall kicks; Stacking bricks.

## Slice 6: Match Logic
**Goal:** The core game mechanic.
- Implement `MatchDetector` (3+ symbols, H/V).
- Implement `MatchRemover` (Clear cells, no gravity shift).
- **Tests:** Horizontal match clears; Vertical match clears; Mixed matches.

## Slice 7: End-to-End Verification & Polish
**Goal:** Full RFC compliance.
- Handle "Spawn Failure" edge case.
- Verify "Game End" messages.
- Final code cleanup and documentation.
- **Tests:** Full game simulation.
# Implementation Plan: Vertical Slices

This document outlines the step-by-step implementation plan for the Match-3 Falling Bricks Game.
Each slice represents a shippable, testable increment of functionality.

## Slice 1: Skeleton & Build Fix (DONE)
**Goal:** Establish a working build environment.
- Fix `build.gradle.kts` syntax errors.
- Configure JUnit 5.
- Create empty `Main.kt`.

## Slice 2: Domain Primitives & Input Parsing (DONE)
**Goal:** Model the core data structures and parse user input.
- Implement `Field`, `Brick`, `Coordinate`.
- Implement `InputParser`.

## Slice 3: RFC Compliance & Renderer Cleanup (CURRENT)
**Goal:** Fix strict RFC violations and prepare for the Game Loop.
- **Fix:** Remove `Square` and `Diamond` from `Symbol` enum (RFC 2.2).
- **Refactor:** Delete `ConsoleRenderer` (incomplete). Move `FieldRenderer` to `render/` package and rename to `ConsoleRenderer`.
- **Verify:** Ensure `GameEngine` uses the renderer that correctly displays the *Active Brick*.

## Slice 4: The Game Loop (Main.kt)
**Goal:** Implement the interactive shell to make the game playable.
- **Implement:** `Main.kt` entry point.
- **Input:** Handle the "Init" prompt (`<width> <height> ...`).
- **Loop:** Implement the "Frame" loop:
    1. Render Field.
    2. Read Input (Buffer commands).
    3. Engine Tick.
- **Constraint:** Enforce "Max 2 valid commands per frame" rule in `Main.kt` (or a `InputController` helper).

## Slice 5: Game End & Restart
**Goal:** Handle the lifecycle transitions defined in RFC.
- **Implement:** "Spawn Failure" detection in `Main.kt`.
- **Implement:** S/Q (Start Over / Quit) prompt logic.
- **Refactor:** Ensure `GameEngine` can be cleanly reset or recreated.

## Slice 6: Final Verification
**Goal:** Full RFC compliance check.
- **Test:** Verify "Game End" messages match RFC exactly.
- **Test:** Full game simulation with a known seed/input sequence.
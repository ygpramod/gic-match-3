# Match-3 Falling Bricks Game — Requirements Specification

## 1. Authority and Scope

This document is the single source of truth for all functional and behavioral requirements.
The implementation must strictly follow this specification. Do not add, modify, or infer requirements beyond what is explicitly defined here.

---

# 2. Definitions

## 2.1 Field

The Field is a rectangular grid defined by:

- Width (integer)
- Height (integer)

Each cell may contain:

- A symbol
- Or be empty

Coordinates:

- Top row = row 0
- Bottom row = row (height − 1)
- Left column = column 0
- Right column = column (width − 1)

---

## 2.2 Symbols

Allowed symbols:

^
*
@
~

No other symbols are permitted.

---

## 2.3 Brick

A Brick consists of exactly:

- 3 blocks
- Each block contains one symbol

Brick has:

Orientation:

- Horizontal (H)
- Vertical (V)

Examples:

H^^*
V*@^

### 2.3.1 Parsing Rules

For a brick string input (e.g., `HABC` or `VABC` where A, B, C are symbols):

**Horizontal (H):**
- Symbol A is at relative (row 0, col 0) (Left)
- Symbol B is at relative (row 0, col 1) (Center)
- Symbol C is at relative (row 0, col 2) (Right)

**Vertical (V):**
- Symbol A is at relative (row 0, col 0) (Top)
- Symbol B is at relative (row 1, col 0) (Middle)
- Symbol C is at relative (row 2, col 0) (Bottom)

---

## 2.4 Active Brick

The Active Brick is the brick currently falling and controllable by the user.
Only one active brick exists at any time.

---

## 2.5 Stationary Brick

A brick becomes stationary when it cannot move down any further.
Stationary bricks remain fixed in the field unless removed by match removal.

---

# 3. Game Initialization

## 3.1 User Input

At game start, the system must prompt:

Please enter field size (width and height) and up to 5 bricks set:

Example:

5 8 H^^* V*@^

Input format:

`<width> <height> <brick1> <brick2> ... <brick5>`

Constraints:

- Width > 0
- Height > 0
- Maximum 5 bricks
- Minimum 0 bricks
- Each brick must follow valid brick format

### 3.1.1 Error Handling

If input is invalid (wrong format, bounds violation, invalid symbols):
1. Display an error message indicating the failure.
2. Re-prompt the user for input (return to start of Section 3.1).
3. Do not start the game until valid input is received.

---

# 4. Brick Spawn Rules

## 4.1 Horizontal Brick Spawn

Horizontal bricks spawn:

- Centered horizontally
- In row 0

## 4.2 Vertical Brick Spawn

Vertical bricks spawn:

- Centered horizontally
- Occupying rows 0, 1, 2

## 4.3 Spawn Failure Condition

If any required spawn cell is already occupied:
1. The game ends immediately.
2. Proceed to **Section 12 (Game End Behavior)**.

---

# 5. Game Loop

The game runs in frames.

Each frame executes in the following order:

1. Display field and active brick
2. Prompt user for commands
3. Process commands
4. Apply automatic drop
5. Check for lock condition
6. Perform match detection and removal
7. Spawn next brick if needed

---

# 6. Command Processing

User prompt:

Enter up to 2 commands to process before moving to the next frame (valid commands are L,R,D)

Allowed commands:

L
R
D

Rules:

- Maximum 2 valid commands per frame
- Ignore invalid commands
- Ignore commands beyond first 2 valid commands

---

## 6.1 Command Behavior

### L — Move Left

Move brick left by 1 column if:

- Within field bounds
- No collision with stationary brick

Otherwise, ignore command.

---

### R — Move Right

Move brick right by 1 column if:

- Within bounds
- No collision

Otherwise, ignore command.

---

### D — Drop

Move brick downward repeatedly until:

- Bottom reached, OR
- Collision with stationary brick

Final position must be valid.

---

# 7. Automatic Drop

After command processing, the active brick must automatically move down by exactly one row if possible.
If it cannot move down:

Brick becomes stationary.

---

# 8. Lock Condition

A brick becomes stationary when:

- It cannot move down further due to:
  - Bottom boundary, OR
  - Collision with stationary brick

Once stationary:

- It becomes part of the field
- It is no longer controllable

---

# 9. Match Detection

After a brick becomes stationary, detect matches.
Match definition:

A match occurs when:

- 3 or more identical symbols
- In straight line
- Horizontal OR vertical

Diagonal matches are NOT valid.

---

# 10. Match Removal

When matches are detected:

- Remove all matching cells
- Set cells to empty

Important constraint:

Gravity must NOT be applied after removal.
Remaining cells stay in their current positions.

---

# 11. Next Brick Spawn

After match removal:

- Spawn next brick from input list

If no bricks remain:

Game ends.

If spawn fails due to blocked position:

Game ends.

---

# 12. Game End Behavior

When game ends, system must prompt:

Enter S to start over or Q to quit

If user enters:

S

System restarts game from initialization.

If user enters:

Q

System exits and prints:

Thank you for playing Match-3!

---

# 13. Field Display Requirement

Each frame must display:

- Current field state
- Active brick position

Display format is implementation-defined but must clearly show:

- Symbols
- Empty cells
- Brick placement

---

# 14. Functional Requirements Summary

The system must support:

- Field initialization
- Brick spawning
- Command processing
- Automatic drop
- Collision detection
- Locking bricks
- Match detection
- Match removal
- Sequential brick spawning
- Game termination
- Restart functionality
- Exit functionality

---

# 15. Non-Functional Requirements

The system must be:

- Deterministic
- Testable
- Modular
- Suitable for microservice deployment
- Runnable in Docker
- Testable using unit, integration, acceptance, and end-to-end tests

---

# End of Specification
# 🕹️High-Performance Tetris

An advanced Java implementation of the classic game Tetris

---

## ⚡ Core Architecture

The system is split into three main object-oriented layers, focusing heavily on execution speed and algorithmic efficiency:

### 🧱 Part A: The `Piece` Class
Represents a single Tetris piece in a specific orientation.
*   **Immutable Design:** Rotation operations do not modify the existing piece object; instead, they utilize pre-computed states.
*   **Fast Rotations:** Rotations are linked via a circular list structure (`.next` pointers) to allow rapid cycling without real-time re-allocation.
*   **Lazy Evaluation:** The standard 7 pieces and their structural variants are initialized safely using the Singleton pattern upon the first client request.

### 🎛️ Part B: The `Board` Class
Manages the internal state of the 2D game grid using a Cartesian coordinate system.
*   **Constant-Time Accessors ($O(1)$):** Tracks structures like `widths` (blocks per row) and `heights` (blocks per column) to evaluate drops, row clears, and max heights instantly.
*   **1-Deep Undo Engine:** Implements a highly optimized transactional backup system. Instead of deeply copying data arrays back during an `undo()`, it fast-swaps pointers between main and backup references to keep performance overhead near zero.
*   **Internal Redundancy Guard:** Includes a macro-controlled `sanityCheck()` method to dynamically trace structural health during execution and isolate logic bugs.

### 🧠 Part C: Brain & The Adversary
Builds smart features on top of the base mechanics to enable automated gameplay and dynamic difficulty scaling:
*   **AI Auto-Player (`JBrainTetris`):** Leverages a decoupled scoring algorithm (`DefaultBrain`) that computes board weights (penalizing columns with high averages or buried holes) to dynamically slide and rotate pieces into optimal slots on every tick.
*   **The Adversary Mode:** Introduces a competitive slider (0% to 100%). When triggered, the engine instantly evaluates up to 175 prospective board configurations within a fraction of a second to programmatically hand the user the worst possible upcoming piece.

---

## 📂 Deliverables & Project Quality

*   **Robust Diagnostic Testing:** Contains explicit JUnit suites (`PieceTest` and `BoardTest`) verifying method edge-cases with comprehensive assertions.
*   **Decoupled Control UI:** Integrates flawlessly with `JTetris`, adding custom UI components to balance speed, toggle AI behavior, and adjust adversarial behavior.

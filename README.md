# 🐫 Arimaa Java Application

A complete Java implementation of the board game **Arimaa** with additional multiplayer functionality over a local network. The project features a GUI for local play, replay capability, and a server-client architecture to allow two players to compete across the same LAN.

---

## 🎮 Game Overview

**Arimaa** is a strategic board game inspired by chess but designed to be difficult for computers to master. Players move animal pieces (Rabbits, Cats, Dogs, Horses, Camels, Elephants) to push, pull, and trap opponents’ pieces, aiming to advance a Rabbit to the far side of the board.

---

## ✨ Features

✅ **Single-player Local Play**  
✅ **Multiplayer over Local Network** (server waits for two clients and relays moves)  
✅ **Graphical User Interface** with multiple panels:
- Welcome screen
- Game board
- Game mode selection
- Replay controls
- Rules display

✅ **Game State Management:**
- Saving and loading games
- Move validation
- Turn management

✅ **Countdown Timer:**
- Per-turn time control

✅ **Replay Functionality:**
- Navigate through move history

---

## 🏗️ Technologies Used

- **Language:** Java
- **GUI Framework:** Swing
- **Networking:** Java Sockets (Server & Client communication)
- **Serialization:** Transmitting game state objects over network streams
- **Concurrency:** Threads for managing connections and UI responsiveness
- **Design Patterns:**
  - MVC (Model-View-Controller)
  - Observer pattern (game listeners)

---

## 📂 Project Structure

**Core Classes:**

- `GameModel`: Maintains the board state, move logic, and game rules.
- `Board`: Represents the board and its cells.
- `Move` / `ComplexMove`: Encapsulate player actions.
- `Directions`, `Offset2D`: Utility classes for movement calculations.
- `GameState`: Enum for different game phases.

**Networking:**

- `Server`: Listens for connections, coordinates message passing between clients.
- `Client`: Connects to the server and exchanges game state updates.

**User Interface:**

- `Launcher`: Starts the application.
- `GameView`: Main frame.
- `BoardPanel`: Renders the board.
- `GameControllerPanel`: Controls and buttons during gameplay.
- `GameModePanel`: Game mode selection.
- `ReplayControllerPanel`: Replay navigation.
- `RulesPanel`: Shows game rules.
- `WelcomePanel`: Entry screen.

**Persistence:**

- `Loader`: Loads saved games.
- `Saver`: Saves current game state.

# FengWei Task Manager

> A personal task management application built in Java with both command-line and graphical user interfaces.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Usage](#usage)
  - [Command Format](#command-format)
  - [Adding Tasks](#adding-tasks)
  - [Managing Tasks](#managing-tasks)
  - [Finding Tasks](#finding-tasks)
- [Commands Reference](#commands-reference)
- [File Storage](#file-storage)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## About

FengWei is a personal task management application that helps you organize your daily tasks. It supports three types of tasks:

- **Todo tasks** - Simple tasks without deadlines
- **Deadline tasks** - Tasks with specific deadlines
- **Event tasks** - Tasks that occur during specific time periods

The application features both a command-line interface for quick interactions and a graphical user interface for a more user-friendly experience.

## Features

- ✅ **Multiple Task Types**: Support for todos, deadlines, and events
- 📅 **Date and Time Management**: Schedule tasks with specific deadlines and time periods
- 🔍 **Search Functionality**: Find tasks by keywords
- 💾 **Automatic Saving**: Tasks are automatically saved to local storage
- 🖥️ **Dual Interface**: Both CLI and GUI modes available
- ⚡ **Fast and Lightweight**: Quick startup and responsive performance
- 🛡️ **Error Handling**: Robust error handling with helpful error messages

## Getting Started

### Prerequisites

- Java 11 or higher
- Gradle (optional, wrapper included)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/fengwei.git
   cd fengwei
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run tests**
   ```bash
   ./gradlew test
   ```

### Running the Application

#### GUI Mode (Recommended)
```bash
./gradlew run
```

#### Command Line Mode
```bash
java -jar build/libs/fengwei.jar
```

## Usage

### Command Format

Commands follow this general format:
```
command [arguments]
```

- Words in `[square brackets]` are optional parameters
- Date and time format: `YYYY-MM-DD HHMM` (e.g., `2025-12-25 1400`)

### Adding Tasks

#### Add a Todo Task
```
todo <description>
```

**Example:**
```
todo Buy groceries
```

**Expected Output:**
```
Got it. I've added this task:
  [T][ ] Buy groceries
Now you have 1 tasks in the list.
```

#### Add a Deadline Task
```
deadline <description> /by <date and time>
```

**Example:**
```
deadline Submit assignment /by 2025-10-15 2359
```

**Expected Output:**
```
Got it. I've added this task:
  [D][ ] Submit assignment (by: Oct 15 2025 2359)
Now you have 2 tasks in the list.
```

#### Add an Event Task
```
event <description> /from <start time> /to <end time>
```

**Example:**
```
event Team meeting /from 2025-09-25 1400 /to 2025-09-25 1600
```

**Expected Output:**
```
Got it. I've added this task:
  [E][ ] Team meeting (from: Sep 25 2025, 14:00 to: Sep 25 2025, 16:00)
Now you have 3 tasks in the list.
```

### Managing Tasks

#### List All Tasks
```
list
```

**Expected Output:**
```
Here are the tasks in your list:
1.[T][ ] Buy groceries
2.[D][ ] Submit assignment (by: Oct 15 2025 2359)
3.[E][ ] Team meeting (from: Sep 25 2025, 14:00 to: Sep 25 2025, 16:00)
```

#### Mark Task as Done
```
mark <task number>
```

**Example:**
```
mark 1
```

**Expected Output:**
```
Nice! I've marked this task as done:
  [T][X] Buy groceries
```

#### Mark Task as Not Done
```
unmark <task number>
```

**Example:**
```
unmark 1
```

**Expected Output:**
```
OK, I've marked this task as not done yet:
  [T][ ] Buy groceries
```

#### Delete a Task
```
delete <task number>
```

**Example:**
```
delete 2
```

**Expected Output:**
```
Noted. I've removed this task:
  [D][ ] Submit assignment (by: Oct 15 2025 2359)
Now you have 2 tasks in the list.
```

### Finding Tasks

#### Search for Tasks
```
find <keyword>
```

**Example:**
```
find meeting
```

**Expected Output:**
```
Here are the matching tasks in your list:
1.[E][ ] Team meeting (from: Sep 25 2025, 14:00 to: Sep 25 2025, 16:00)
```

## Commands Reference

| Command | Format | Description | Example |
|---------|--------|-------------|---------|
| `todo` | `todo <description>` | Add a todo task | `todo Read book` |
| `deadline` | `deadline <description> /by <date time>` | Add a deadline task | `deadline Project /by 2025-12-01 2359` |
| `event` | `event <description> /from <start> /to <end>` | Add an event task | `event Conference /from 2025-11-01 0900 /to 2025-11-01 1700` |
| `list` | `list` | Show all tasks | `list` |
| `mark` | `mark <task number>` | Mark task as done | `mark 3` |
| `unmark` | `unmark <task number>` | Mark task as not done | `unmark 3` |
| `delete` | `delete <task number>` | Delete a task | `delete 2` |
| `find` | `find <keyword>` | Search for tasks | `find book` |
| `bye` | `bye` | Exit the application | `bye` |

## File Storage

FengWei automatically saves your tasks to a local file:

- **Storage Location**: `./data/Tasks.txt`
- **Format**: Human-readable text format
- **Automatic Backup**: Tasks are saved after every modification

### Storage Format Example
```
T | 0 | Buy groceries
D | 1 | Submit assignment | Oct 15 2025 2359
E | 0 | Team meeting | 2025-09-25T14:00 | 2025-09-25T16:00
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── FengWei.java          # Main application class
│   │   ├── Launcher.java         # Application launcher
│   │   ├── Main.java             # CLI entry point
│   │   ├── MainWindow.java       # GUI main window
│   │   ├── DialogBox.java        # GUI dialog component
│   │   ├── exceptions/
│   │   │   └── FengWeiException.java  # Custom exception class
│   │   ├── parser/
│   │   │   └── Parser.java       # Command parser
│   │   ├── storage/
│   │   │   └── TasksStorage.java # File I/O operations
│   │   ├── tasks/
│   │   │   ├── Task.java         # Base task class
│   │   │   ├── TodoTask.java     # Todo task implementation
│   │   │   ├── DeadlineTask.java # Deadline task implementation
│   │   │   ├── EventTask.java    # Event task implementation
│   │   │   └── TaskList.java     # Task collection manager
│   │   └── ui/
│   │       └── Ui.java           # User interface handler
│   └── resources/
│       ├── images/               # Application images
│       └── view/                 # FXML files for GUI
└── test/
    └── java/
        └── tasks/                # Unit tests
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Add unit tests for new features
- Update documentation for API changes
- Use meaningful commit messages

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Built with ❤️ using Java and JavaFX**

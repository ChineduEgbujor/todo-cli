# Java CLI To-Do List Manager

A simple, persistent command-line application for managing tasks with support for tagging, filtering, and logging. Built with Java, Picocli, Jackson, and JUnit.

---

## Table of Contents

1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Installation & Build](#installation--build)
4. [Usage](#usage)
    - [Running the JAR](#running-the-jar)
    - [Maven Exec (during development)](#maven-exec-during-development)
    - [Global Alias or Batch Script](#global-alias-or-batch-script)
5. [Commands](#commands)
    - [`add`](#add)
    - [`list`](#list)
    - [`done`](#done)
    - [`delete`](#delete)
6. [Data Storage & Logging](#data-storage--logging)
7. [Running Tests](#running-tests)
8. [Project Structure](#project-structure)
9. [Extending the App](#extending-the-app)
10. [License](#license)

---

## Features

- **Persistent storage**  
  Tasks are saved to a JSON file (`~/.todo-cli/tasks.json`) so data survives between runs.

- **In-memory and file-based repositories**  
  During development, an in-memory repository is used for fast testing. In production, tasks are stored/retrieved via Jackson into a local JSON file.

- **Tagging & filtering**  
  • Assign one or more tags when adding a task (`--tags work,urgent`).  
  • Filter the task list by status (`--pending`, `--done`) or by a specific tag (`--tag work`).

- **Subcommands via Picocli**  
  • `add` – create a new task (with optional tags)  
  • `list` – display tasks (all, pending, done, or by tag)  
  • `done` – mark a task as completed  
  • `delete` – remove a task by ID

- **Logging**  
  Application logs (INFO and above) are written to both console and a daily-rotated log file (`~/.todo-cli/todo.log`).

- **Unit tests**  
  JUnit 5 test suites cover in-memory repository behavior, file persistence, and filtering logic (≈ 90% code coverage).

---

## Prerequisites

- **Java 17+** (JDK installed, `java` and `javac` on your PATH)
- **Maven 3.x** (or rely on IntelliJ’s bundled Maven for IDE builds)
- **Git** (optional, if cloning from a repository)

---

## Installation & Build

1. **Clone the repository** (if applicable):
   ```bash
   git clone https://github.com/your-username/todo-cli.git
   cd todo-cli
2. Build & package (produces a “fat” JAR with dependencies):
    ```bash
   mvn clean package
3. Verify build success. 
   You should see output similar to:
    ```bash
   [INFO] BUILD SUCCESS
   [INFO] Total time:  10.123 s
   [INFO] Finished at: 2025-06-01T12:00:00Z
   
---

## Usage

- **Running the JAR**  
  From your project directory (where `pom.xml` and `target/` live), execute:
  ```bash
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar <subcommand> [options]
    ```

  Examples
    ```bash
  # add tasks
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar add "Buy groceries" --tags personal,errand
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar add "Submit expense report" --tags work,urgent
  
  # list all tasks
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar list
  
  # list only pending tasks
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar list --pending
  
  # list only done tasks
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar list --done
  
  # list tasks by tag
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar list --tag work
  
  # mark a task done
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar done 2
  
  # delete a task
  java -jar target/todo-cli-1.0-SNAPSHOT-shaded.jar delete 3
  ```


- **Project Structure**  
  ```bash
  todo-cli/
  ├─ pom.xml
  ├─ README.md
  ├─ src/
  │  ├─ main/
  │  │  ├─ java/com/chinedu/todo/
  │  │  │  ├─ cli/           (Picocli commands: AddCommand, ListCommand, DoneCommand, DeleteCommand, RootCommand, TodoApp)
  │  │  │  ├─ model/         (Task.java)
  │  │  │  ├─ repository/    (TaskRepository.java, InMemoryTaskRepository.java, FileTaskRepository.java)
  │  │  │  └─ … (any additional utility classes)
  │  │  └─ resources/
  │  │     └─ logback.xml    (SLF4J/Logback configuration)
  │  └─ test/
  │     └─ java/com/chinedu/todo/
  │        ├─ repository/    (InMemoryTaskRepositoryTest.java, FileTaskRepositoryTest.java)
  │        ├─ cli/           (ListCommandTest.java, AddCommandTest.java, etc.)
  │        └─ model/         (TaskTest.java, if any)
  └─ target/                (compiled classes and packaged JARs)
    ```
---

## Extending the App
- **CSV Export**  
  Implement an ExportCommand that writes tasks (optionally filtered) to a CSV file.

  • Use OpenCSV or manually assemble comma-separated lines.

  • Accept an `--out <file.csv>` option.


- **JavaFX Desktop UI**  
  Create a JavaFX application that uses FileTaskRepository to show tasks in a `TableView`.

  • Add controls for Add/List/Done/Delete.

  • Package via `jpackage` to produce a native installer.

- **Docker & CI/CD**

  • Write a `Dockerfile` that packages the JAR into a lightweight container.
  • push to Docker Hub or a private registry.
  • Add a GitHub Actions workflow (`.github/workflows/ci.yml`) to run `mvn test` and build the shaded JAR on every push.

## License
- This project is released under the MIT License.


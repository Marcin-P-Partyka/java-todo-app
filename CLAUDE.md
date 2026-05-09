# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build (creates fat JAR with all dependencies)
mvn package

# Run from built JAR (must use a real Linux/Windows terminal — not IntelliJ's embedded terminal)
java -jar target/todo-app-1.0-SNAPSHOT-jar-with-dependencies.jar

# Compile only
mvn compile

# Clean build artifacts
mvn clean
```

There are no tests in this project.

## Architecture

The app is a console-based task manager written in Java 25, using Maven. Dependencies: **Gson** (JSON persistence) and **JLine** (raw terminal input).

**Wiring (Main.java):** `FileStorage` → `TaskManager` → `ConsoleUI`. Each layer depends only on the one below it.

**Data flow:** Every mutation in `TaskManager` (add, complete, delete) immediately calls `storage.save(listaZadan)`. On startup, `TaskManager` loads from disk and derives the next ID from `max(existing IDs) + 1`. The `tasks.json` file is written to the **current working directory** (not `target/`).

**FileStorage** uses a custom Gson `TypeAdapter` for `LocalDate` because Gson doesn't handle it natively.

**ConsoleUI** uses JLine's raw terminal mode for arrow-key navigation. This requires a real system terminal — it does not work inside IntelliJ's embedded terminal. The UI shows a two-column layout: task list on the left, details of the currently selected task on the right.

**Work in progress:** The UI is mid-refactor. The old Scanner-based numeric menu (1=add, 2=complete, 3=delete, 0=quit) is commented out in `ConsoleUI.start()`. The new keyboard-driven version handles arrow keys (up/down to navigate, `q` to quit) but the `'a'` (add) action is a stub (`// showPopup(...)`). CRUD via keyboard is not yet implemented.

## Field naming

Model fields and local variables use Polish names (`nazwa`=name, `opis`=description, `waznosc`=priority, `status`=done flag, `czasTrwania`=duration, `dataUtworzenia`=creation date, `deadline`=deadline). This is intentional — keep new code consistent with the existing style.

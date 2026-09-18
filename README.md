# Smart Expense Tracker

A simple, terminal-based personal expense tracking application built in
core Java. This project was created as a college course project to
demonstrate fundamental Java and object-oriented programming concepts
without relying on any database, GUI library, or external framework.

## Overview

Smart Expense Tracker lets a user record daily expenses, view them in a
clean tabular format, search by category, calculate totals, view a
category-wise spending summary, and delete records — all from a simple
numbered console menu. Data is stored locally in a plain CSV file
(`data/expenses.csv`), so the application runs anywhere a JDK is
installed, with no setup or external dependencies.

## Features

- Add a new expense (ID, date, category, description, amount)
- View all expenses in a formatted table
- Search/filter expenses by category (partial, case-insensitive match)
- Calculate and display the total of all expenses
- Category-wise spending summary
- Delete an expense by its ID
- Automatic loading of existing data on startup and saving after every change
- Graceful handling of invalid input (bad numbers, empty fields, missing file, etc.)

## Java Concepts Demonstrated

- **Classes and Objects** — `Expense` models a single expense record
- **Encapsulation** — private fields with public getters/setters
- **Constructors** — used to build `Expense` objects
- **Methods** — clear separation of responsibilities across classes
- **ArrayList / Collections** — `ArrayList<Expense>`, `LinkedHashMap` for summaries
- **File Handling** — reading/writing CSV using `BufferedReader` / `FileWriter`
- **Exception Handling** — `try/catch`, checked exceptions
- **Custom Exception** — `InvalidExpenseException` for validation errors
- **Loops and Conditionals** — menu loop, validation logic
- **String Handling** — parsing, formatting, CSV sanitizing
- **Basic OOP Design** — each class has a single, clear responsibility

## Project Structure

```
SmartExpenseTracker/
├── src/
│   ├── Main.java                  # Entry point, console menu, user interaction
│   ├── Expense.java                # Expense model (encapsulation, CSV conversion)
│   ├── ExpenseManager.java         # Business logic (add, delete, search, summarize)
│   ├── FileHandler.java            # Reads/writes expenses.csv
│   └── InvalidExpenseException.java# Custom checked exception
├── data/
│   └── expenses.csv                # Local data storage (auto-created if missing)
├── README.md
└── .gitignore
```

### Class Responsibilities

| Class                      | Responsibility                                              |
|-----------------------------|---------------------------------------------------------------|
| `Expense`                   | Holds data for one expense; converts to/from CSV format       |
| `ExpenseManager`             | Maintains the list of expenses; add/delete/search/summarize   |
| `FileHandler`                | Loads from and saves to `data/expenses.csv`                   |
| `InvalidExpenseException`    | Thrown when expense data fails validation                     |
| `Main`                       | Displays the menu and connects user input to `ExpenseManager` |

## Prerequisites

- Java Development Kit (JDK) 8 or later installed
- A terminal / command prompt (or VS Code with a Java extension)

Check your Java version with:

```
java -version
javac -version
```

## How to Compile and Run

These commands work the same in Windows Command Prompt, PowerShell, macOS
Terminal, or Linux shell. Run them from the `SmartExpenseTracker` project
root folder (the folder containing `src` and `data`).

**1. Compile:**

```
javac -d out src/*.java
```

This compiles all `.java` files from `src` and places the resulting
`.class` files in a new `out` folder.

**2. Run:**

```
java -cp out Main
```

The application reads from and writes to `data/expenses.csv`, so always
run the `java` command from the `SmartExpenseTracker` root folder (not
from inside `src` or `out`) so that the relative path `data/expenses.csv`
resolves correctly.

### Running in VS Code

1. Open the `SmartExpenseTracker` folder in VS Code.
2. Install the "Extension Pack for Java" (by Microsoft) if you don't have it.
3. Open `src/Main.java` and click **Run** above the `main` method, or use
   the terminal commands above inside VS Code's integrated terminal.

## How Data Storage Works

- All expenses are stored as plain text in `data/expenses.csv`, one
  expense per line, in the format:
  ```
  ID,Date,Category,Description,Amount
  ```
- If `data/expenses.csv` (or the `data` folder) does not exist, the
  program creates it automatically the first time it runs.
- On startup, `FileHandler` reads the CSV file and loads existing
  expenses into memory. Any malformed line is skipped with a warning
  instead of crashing the program.
- After every add or delete operation, the full expense list is
  re-written to the CSV file, so the file always reflects the current
  state of the application.
- Commas typed inside a category or description are automatically
  replaced with semicolons before saving, so the CSV structure is never
  broken.

## Example Usage

```
===== SMART EXPENSE TRACKER =====
1. Add Expense
2. View All Expenses
3. Search by Category
4. Show Total Expenses
5. Category-wise Summary
6. Delete Expense
7. Exit
Enter your choice: 1

--- Add New Expense ---
Enter date (yyyy-MM-dd): 2026-09-18
Enter category (e.g., Food, Travel, Bills): Food
Enter description: Lunch with friends
Enter amount: 250
Expense added successfully with ID: 1

===== SMART EXPENSE TRACKER =====
...
Enter your choice: 2

--- All Expenses ---
ID    Date         Category        Description               Amount
---------------------------------------------------------------------
1     2026-09-18   Food            Lunch with friends           250.00
```

## Future Enhancements

- Support editing an existing expense instead of only add/delete
- Add monthly/date-range filtering
- Export category summary as a separate report file
- Add simple input-based sorting (by date or amount)
- Optional budget limit warnings per category

## Author's Note

This project was built to be small, readable, and fully understandable
for a college submission — it intentionally avoids databases, GUI
frameworks, and external libraries, relying only on core Java (JDK
standard library).

# Janet User Guide

Janet is a task manager that keeps track of to-dos, deadlines, and events. Enter commands in the message box and
press Enter or click Send. Janet saves every change automatically.

## Commands

| Action | Command | Example |
| --- | --- | --- |
| Add a to-do | `todo DESCRIPTION` | `todo read book` |
| Add a deadline | `deadline DESCRIPTION /by YYYY-MM-DD` | `deadline return book /by 2019-12-02` |
| Add an event | `event DESCRIPTION /from START /to END` | `event meeting /from Mon 2pm /to 4pm` |
| List tasks | `list` | `list` |
| Find tasks | `find KEYWORD` | `find book` |
| Mark a task | `mark NUMBER` | `mark 2` |
| Unmark a task | `unmark NUMBER` | `unmark 2` |
| Delete tasks | `delete NUMBER [NUMBER ...]` | `delete 2 4 5` |
| Exit | `bye` | `bye` |

## Deleting multiple tasks

Use one `delete` command followed by the displayed numbers of all tasks to remove. For example:

```text
delete 2 4 5
```

Janet validates the entire selection before changing the task list. Each number must be an integer that identifies an
existing task, and the same number cannot appear twice. If any number is malformed, duplicated, or out of range, Janet
shows an error and removes nothing. A single-task command such as `delete 2` continues to work as before.

Task numbers refer to the list as it appeared before the command. Removed tasks are confirmed in the order entered,
while the remaining tasks keep their relative order.

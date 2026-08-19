# Janet UI test plan

The runner executes each test case in order. Expected output is compared exactly.

## Test case: Add, list, mark, and unmark a task

Aim: Verify that Janet stores a task, displays its status, marks it done, and reverses the status.

### Inputs

```text
todo read book
list
mark 1
unmark 1
list
bye
```

### Expected output

```text
____________________________________________________________
     _                  _
    | |                | |
    | | __ _ _ __   ___| |_
 _  | |/ _` | '_ \ / _ \ __|
| |_| | (_| | | | |  __/ |_
 \___/ \__,_|_| |_|\___|\__|
____________________________________________________________
Hi! I'm Janet! I'm here to help with absolutely anything.
What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
 Okay, I've marked this task as not done yet:
   [T][ ] read book
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Delete a task

Aim: Verify that Janet removes the selected task and reports the updated task count.

### Inputs

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
delete 3
list
bye
```

### Expected output

```text
____________________________________________________________
     _                  _
    | |                | |
    | | __ _ _ __   ___| |_
 _  | |/ _` | '_ \ / _ \ __|
| |_| | (_| | | | |  __/ |_
 \___/ \__,_|_| |_|\___|\__|
____________________________________________________________
Hi! I'm Janet! I'm here to help with absolutely anything.
What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
 Noted. I've removed this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 2 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Add and complete an event

Aim: Verify that Janet creates an event, displays its start and end times, and preserves them when marking it done.

### Inputs

```text
event project meeting /from Mon 2pm /to 4pm
list
mark 1
list
bye
```

### Expected output

```text
____________________________________________________________
     _                  _
    | |                | |
    | | __ _ _ __   ___| |_
 _  | |/ _` | '_ \ / _ \ __|
| |_| | (_| | | | |  __/ |_
 \___/ \__,_|_| |_|\___|\__|
____________________________________________________________
Hi! I'm Janet! I'm here to help with absolutely anything.
What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 1 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
 Nice! I've marked this task as done:
   [E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
 Here are the tasks in your list:
 1.[E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Add and complete a deadline

Aim: Verify that Janet creates a deadline, displays its due date, and preserves that information when marking it done.

### Inputs

```text
deadline return book /by Sunday
list
mark 1
list
bye
```

### Expected output

```text
____________________________________________________________
     _                  _
    | |                | |
    | | __ _ _ __   ___| |_
 _  | |/ _` | '_ \ / _ \ __|
| |_| | (_| | | | |  __/ |_
 \___/ \__,_|_| |_|\___|\__|
____________________________________________________________
Hi! I'm Janet! I'm here to help with absolutely anything.
What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [D][ ] return book (by: Sunday)
 Now you have 1 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Sunday)
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Sunday)
____________________________________________________________
 Here are the tasks in your list:
 1.[D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Add and complete a to-do

Aim: Verify that Janet creates a to-do, displays its type icon, and preserves that type when marking it done.

### Inputs

```text
todo borrow book
list
mark 1
list
bye
```

### Expected output

```text
____________________________________________________________
     _                  _
    | |                | |
    | | __ _ _ __   ___| |_
 _  | |/ _` | '_ \ / _ \ __|
| |_| | (_| | | | |  __/ |_
 \___/ \__,_|_| |_|\___|\__|
____________________________________________________________
Hi! I'm Janet! I'm here to help with absolutely anything.
What can I do for you?
____________________________________________________________
 Got it. I've added this task:
   [T][ ] borrow book
 Now you have 1 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] borrow book
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] borrow book
____________________________________________________________
 Here are the tasks in your list:
 1.[T][X] borrow book
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

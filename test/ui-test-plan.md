# Janet UI test plan

The runner executes each test case in order. Expected output is compared exactly.
Each case runs in a fresh temporary folder. Cases may provide an initial data file or assert the saved file.

## Test case: Find tasks by keyword

Aim: Verify that Janet displays tasks whose descriptions contain a keyword and rejects an empty keyword without changing the list.

### Inputs

```text
todo read book
deadline return book /by 2019-12-02
event project meeting /from Mon 2pm /to 4pm
find book
find
find meeting
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
   [D][ ] return book (by: Dec 02 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Mon 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Dec 02 2019)
____________________________________________________________
 OOPS!!! A find command needs a keyword.
____________________________________________________________
 Here are the matching tasks in your list:
 1.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] return book (by: Dec 02 2019)
 3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Save task changes automatically

Aim: Verify that adding and marking a task writes its current state to Janet's relative data file.

### Inputs

```text
todo read book
mark 1
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
 Nice! I've marked this task as done:
   [T][X] read book
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

### Expected saved data

```text
T	1	read book
```

## Test case: Load saved tasks at startup

Aim: Verify that Janet recreates each stored task and its completion status when it starts.

### Inputs

```text
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
 Here are the tasks in your list:
 1.[T][X] read book
 2.[D][ ] return book (by: Dec 02 2019)
 3.[E][X] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

### Initial saved data

```text
T	1	read book
D	0	return book	2019-12-02
E	1	project meeting	Mon 2pm	4pm
```

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
deadline return book /by 2019-12-02
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
   [D][ ] return book (by: Dec 02 2019)
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
 2.[D][ ] return book (by: Dec 02 2019)
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
deadline return book /by 2019-12-02
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
   [D][ ] return book (by: Dec 02 2019)
 Now you have 1 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] return book (by: Dec 02 2019)
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] return book (by: Dec 02 2019)
____________________________________________________________
 Here are the tasks in your list:
 1.[D][X] return book (by: Dec 02 2019)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

### Expected saved data

```text
D	1	return book	2019-12-02
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

## Test case: Reject empty and unknown commands without changing tasks

Aim: Verify that an empty to-do and an unknown command show helpful errors, while an existing task remains intact.

### Inputs

```text
list
todo
todo read book
what
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
 Here are the tasks in your list:
____________________________________________________________
 OOPS!!! A todo needs a description.
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
 OOPS!!! I don't recognize that command.
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Reject malformed deadline and event commands without changing tasks

Aim: Verify that malformed structured commands are rejected and do not add tasks; valid deadlines and events continue to work.

### Inputs

```text
deadline
deadline submit report /by
deadline submit report /by Friday
deadline submit report /by 2019-12-06
event meeting /from Mon 2pm
event meeting /from Mon 2pm /to
event meeting /from /to 4pm
event meeting /from Mon 2pm /to 4pm
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
 Sorry, please use: deadline <task> /by <date or time>.
____________________________________________________________
 Sorry, please use: deadline <task> /by <date or time>.
____________________________________________________________
 Sorry, please provide a deadline date in yyyy-MM-dd format.
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit report (by: Dec 06 2019)
 Now you have 1 tasks in the list.
____________________________________________________________
 Sorry, please use: event <task> /from <start> /to <end>.
____________________________________________________________
 Sorry, please use: event <task> /from <start> /to <end>.
____________________________________________________________
 OOPS!!! An event needs a description, start, and end.
____________________________________________________________
 Got it. I've added this task:
   [E][ ] meeting (from: Mon 2pm to: 4pm)
 Now you have 2 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
 1.[D][ ] submit report (by: Dec 06 2019)
 2.[E][ ] meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

## Test case: Reject invalid task numbers without changing task state

Aim: Verify that mark, unmark, and delete reject missing, non-numeric, zero, and out-of-range task numbers, while valid operations still change only the selected task.

### Inputs

```text
todo alpha
mark
mark 0
mark 2
mark 1
unmark
unmark zero
unmark 2
unmark 1
delete
delete zero
delete 2
delete 1
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
   [T][ ] alpha
 Now you have 1 tasks in the list.
____________________________________________________________
 Sorry, please provide a valid task number.
____________________________________________________________
 Sorry, that task number does not exist.
____________________________________________________________
 Sorry, that task number does not exist.
____________________________________________________________
 Nice! I've marked this task as done:
   [T][X] alpha
____________________________________________________________
 Sorry, please provide a valid task number.
____________________________________________________________
 Sorry, please provide a valid task number.
____________________________________________________________
 Sorry, that task number does not exist.
____________________________________________________________
 Okay, I've marked this task as not done yet:
   [T][ ] alpha
____________________________________________________________
 Sorry, please provide a valid task number.
____________________________________________________________
 Sorry, please provide a valid task number.
____________________________________________________________
 Sorry, that task number does not exist.
____________________________________________________________
 Noted. I've removed this task:
   [T][ ] alpha
 Now you have 0 tasks in the list.
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

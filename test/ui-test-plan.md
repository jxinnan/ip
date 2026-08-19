# Janet UI test plan

The runner executes each test case in order. Expected output is compared exactly.

## Test case: Add, list, mark, and unmark a task

Aim: Verify that Janet stores a task, displays its status, marks it done, and reverses the status.

### Inputs

```text
read book
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
 added: read book
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] read book
____________________________________________________________
 Nice! I've marked this task as done:
   [X] read book
____________________________________________________________
 Okay, I've marked this task as not done yet:
   [ ] read book
____________________________________________________________
 Here are the tasks in your list:
 1.[ ] read book
____________________________________________________________
____________________________________________________________
 Okay! Have a wonderful day. Bye!
____________________________________________________________
```

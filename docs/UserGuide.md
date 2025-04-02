# PayPals User Guide

## Table of Contents
- [PayPals User Guide](#paypals-user-guide)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Quick Start](#quick-start)
  - [Glossary](#glossary)
  - [Command Reference](#command-reference)
  - [Features](#features)
    - [Expense Tracking](#expense-tracking)
    - [Debt Settlement](#debt-settlement)
    - [Storage](#storage)
  - [Command Format: Group Selection Menu](#command-format-group-selection-menu)
  - [Command Format: Inside A Group](#command-format-inside-a-group)
    - [Viewing help: `help`](#viewing-help-help)
    - [Adding an expense with details: `add`](#adding-an-expense-with-details-add)
    - [Adding an expense with equal portions of spending: `addequal`]()
    - [Delete an expense: `delete`](#delete-an-expense-delete)
    - [List all past expenses: `list`](#list-all-past-expenses-list)
    - [Generate a simplified debt settlement plan: `split`](#generate-a-simplified-debt-settlement-plan-split)
    - [Mark as "paid" when settled: `paid`](#mark-as-paid-when-settled-paid)
    - [Edit description of an activity: `edit`](#edit-description-of-an-activity-edit)
    - [Edit payer name of an activity: `edit`](#edit-payer-name-of-an-activity-edit)
    - [Edit name of a friend that owes: `edit`](#edit-name-of-a-friend-that-owes-edit)
    - [Edit amount of a friend that owes: `edit`](#edit-amount-of-a-friend-that-owes-edit)
    - [Change to Group Selection Menu: `change`](#change-to-group-selection-menu-change)
    - [Exiting the application: `exit`](#exiting-the-application-exit)
  - [Command Tips](#command-tips)
  - [FAQ](#faq)
  - [Common Errors](#common-errors)
    - [Failure to adhere to command format](#failure-to-adhere-to-command-format)
    - [Invalid Requests](#invalid-requests)
  - [Known Issues](#known-issues)

## Introduction

PayPals is a CLI-based application that aims to provide assistance in simplifying payments by minimising the number of transactions needed to balance expenses.

## Quick Start

1. Ensure that you have Java 17 installed on your computer. Mac users can install the JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html)
2. Download the latest .jar file from [here](https://github.com/AY2425S2-CS2113-T13-2/tp/releases/tag/v1.0)
3. Copy the file to the folder you want to use as the home folder for your PayPals application.
4. Open a command terminal, cd into the folder you put the jar file in, and use the following command to run the application:
```
java -jar paypals.jar
```
5. Select a group by entering the number of the group displayed on the console, or create a new group by entering the name of the new group. Press enter to confirm your group.
6. Type the command in the command box and press Enter to execute it. e.g. typing help and pressing Enter will open the help window.
7. You can now start using PayPals!

## Glossary

- **Activity**: A single transaction where one person pays and others owe.
- **Payer**: The person who paid for the activity.
- **Friend**: A person who owes part of the expense.
- **Group**: A logical collection of activities related to a trip or event.
- **Identifier (i/)**: A number used to identify and refer to a specific activity.

## Command Reference

A quick reference table for all commands when inside a group is presented below. For further details on commands inside the group selection menu, please refer to this [section](#command-format-group-selection-menu)

| Task                                            | Command Expression                                                    |
|-------------------------------------------------|-----------------------------------------------------------------------|
| Help menu                                       | `help`                                                                |
| Add an activity                                 | `add d/DESCRIPTION n/NAME f/FRIEND1 a/AMOUNT1 f/FRIEND2 a/AMOUNT2...` |
| Add an activity with equal portions of spending | `addequal d/DESCRIPTION n/NAME f/FRIEND1 f/FRIEND2 ... a/AMOUNT`      |
| Delete an activity                              | `delete i/IDENTIFIER`                                                 |
| List all expenses                               | `list`                                                                |
| List all expenses by a person                   | `list n/NAME`                                                         |
| Split bills                                     | `split`                                                               |
| Mark an activity as paid for a person           | `paid n/NAME i/IDENTIFIER`                                            |
| Mark an activity as unpaid for a person         | `unpaid n/NAME i/IDENTIFIER`                                          |
| Mark an activity as paid for everyone           | `paid n/PAYER i/IDENTIFIER`                                           |
| Edit the description of an activity             | `edit i/IDENTIFIER d/DESCRIPTION`                                     |
| Edit the payer name of an activity              | `edit i/IDENTIFIER n/NEWNAME`                                         |
| Edit the name of a friend that owes money       | `edit i/IDENTIFIER f/NEWNAME o/OLDNAME`                               |
| Edit the amount of a friend that owes money     | `edit i/IDENTIFIER a/NEWAMOUNT o/FRIENDNAME`                          |
| Change to group selection menu                   | `change`                                                              |
| Close the application                           | `exit`                                                                |

## Features 

Notes about the command format:
- Words in UPPER_CASE are the parameters to be supplied by the user. e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John`.
  - Parameters are case-insensitive and cannot contain the symbol `/`.
- Items in square brackets are optional. e.g `n/NAME [t/TAG]` can be used as `n/John t/friend` or as `n/John`.
- Items with ... after them can be used multiple times including zero times. e.g. `[t/TAG]`... can be used as (i.e. 0 times), `t/friend, t/friend t/family` etc.

### Expense Tracking

Our app lets users effortlessly record shared expenses, ensuring everyone’s contributions are accurately tracked.

### Debt Settlement

When it’s time to split the total bill, the app simplifies the process by calculating net balances for each member. It then recommends the fewest necessary transactions to clear debts, helping you streamline payments and reduce the hassle of back-and-forth settlements.

### Storage

PayPals data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.
The file is also created automatically if it does not exist.
Note: Do not edit any of the data files manually as it will result in corrupted files.

### Groups

When the application starts up, PayPals will first ask the user to either select one of the existing groups, or create a new group. If an existing group is selected, 
the data specifically for that group will be loaded. Otherwise, a new group would be created with no data and the user can have a fresh start. 
If there are no existing groups, it will be displayed on the console to the user and the user would have to create a new group to proceed with using the app.
This feature allows users to separate their trips to individual groups to better manage their expense tracking. 

The following is an example of what the user would see when the program starts:

```
Welcome to PayPals!
____________________________________________________________
Would you like to delete or select a group?
____________________________________________________________
>
```

## Command Format: Group Selection Menu
### Selecting a group: `select`
Shows a list of existing groups. Enter the corresponding group name or number after to enter the group. If not, a new group will be created with the input.
  
Example of usage:  
```
Welcome to PayPals!
____________________________________________________________
Would you like to delete or select a group?
____________________________________________________________
> select
Please select a group number from the following:
(1) group1
or give your new group a name:
> 1
____________________________________________________________
You are currently in the "group1" group.
There are 0 transactions.
____________________________________________________________
```  
Or  
```
Welcome to PayPals!
____________________________________________________________
Would you like to delete or select a group?
____________________________________________________________
> select
Please select a group number from the following:
(1) group1
or give your new group a name:
> group2
____________________________________________________________
You are currently in the "group2" group.
It is a new group.
____________________________________________________________
```   
### Deleting A Group: `delete`
Shows a list of existing groups. Enter the corresponding group name or number after to delete the group.  
  
Examples of usage:
```
Welcome to PayPals!
____________________________________________________________
Would you like to delete or select a group?
____________________________________________________________
> delete
Please select a group number from the following:
(1) group1
(2) group2
> group2

```
Or
```
Welcome to PayPals!
____________________________________________________________
Would you like to delete or select a group?
____________________________________________________________
> delete
Please select a group number from the following:
(1) group1
> 1
```
## Command Format: Inside A Group
### Viewing help: `help`

Shows a help message containing all the commands available in PayPals.

Format: `help`

Example of usage:

```
> help
____________________________________________________________
Help - Available Commands:

Commands with format:
  1. add    -> add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_1 f/FRIEND2 a/AMOUNT_2...
  2. addequal   -> addequal d/DESCRIPTION n/PAYER f/FRIEND1 f/FRIEND2 ... a/AMOUNT
  3. delete     -> delete i/IDENTIFIER
  4. paid       -> paid n/NAME i/IDENTIFIER
  5. unpaid     -> unpaid n/NAME i/IDENTIFIER
  6. edit       -> (Description) edit i/IDENTIFIER d/NEWDESCRIPTION
                -> (Payer name) edit i/IDENTIFIER n/NEWNAME
                -> (Friend name) edit i/IDENTIFIER f/NEWNAME o/OLDNAME
                -> (Friend amount) edit i/IDENTIFIER a/NEWAMOUNT o/FRIENDNAME

Commands without format:
  1. list
  2. split
  3. help
  4. exit

For more details, please refer to the User Guide of PayPals.

____________________________________________________________
```

### Adding an expense with details: `add`
Adds an expense.
> Note: Names of payer  and friends are case-sensitive.

Format: `add d/DESCRIPTION n/NAME f/FRIEND1 a/AMOUNT1 f/FRIEND2 a/AMOUNT2...`

Example of usage: 

```
> add d/lunch n/Jane f/John a/28
____________________________________________________________
Desc: lunch
Name of payer: Jane
Number of friends who owe Jane: 1
____________________________________________________________
```

```
> add d/tickets n/John f/Betty a/23.53 f/Jane a/20.21 f/Bob a/38.10
____________________________________________________________
Desc: tickets
Name of payer: John
Number of friends who owe John: 3
____________________________________________________________
```

### Add an expense with equal portions of spending  
Adds an expense with amount split equally among everyone.
> Note:  
> * Names of payer  and friends are case-sensitive.  
> * Amount that each person owe will be rounded to 2 d.p. 

Format: `add d/DESCRIPTION n/NAME f/FRIEND1 f/FRIEND2 ... a/AMOUNT`

Example of usage:

```
> add d/lunch n/Jane f/John a/28
____________________________________________________________
Desc: lunch
Name of payer: Jane
Number of friends who owe Jane: 1
____________________________________________________________
```

```
> add d/tickets n/John f/Betty f/Jane f/Bob a/30.40
____________________________________________________________
Desc: tickets
Name of payer: John
Number of friends who owe John: 3
____________________________________________________________
```
### Delete an expense: `delete`
Deletes an expense.
> Note: if more than one`IDENTIFIER` is entered, only the first one is regarded.

Format: `delete i/IDENTIFIER`

Example of usage: 

```
> list
____________________________________________________________
You have 1 activities:
1.  Desc: lunch
    Payer: Jane
    Owed by: John
____________________________________________________________
> delete i/1
____________________________________________________________
Expense removed successfully!
____________________________________________________________
> list
____________________________________________________________
You currently have no activities.
____________________________________________________________
```
### List all past expenses: `list`
Displays all past expenses for everyone or a single person.

Format: `list [n/NAME]`

* If no name is provided, all past expenses for everyone are displayed.

* If a `NAME` is provided, the list will categorize that person’s activities
  into two sections: `fully paid` and `not fully paid`. An activity is marked 
  as `fully paid` only if all payees have completed their payments to the specified 
  person for that activity. If any payee has not paid, the activity will appear under 
  `not fully paid`.

* If a `NAME` is provided, any activities where the specified person is 
  the payer will display a `[PAYER]` tag next to the activity description.
* If a `NAME` is provided, the `list` command will display the past expenses within
  the current group for `NAME`.
  

Example of usage: 

```
> list
____________________________________________________________
You have 2 activities:
1.  Desc: lunch
    Payer: Jane
    Owed by: John
2.  Desc: tickets
    Payer: John
    Owed by: Bob, Betty, Jane
____________________________________________________________
```
```
> list n/John
____________________________________________________________
Activities which have been fully paid for John:

Activities which have not been fully paid for John:
1.  Desc: lunch
    Payer: Jane
    Amount: $28.00 [Unpaid]
2.  [PAYER] Desc: tickets
    Owed by: Bob [Unpaid] Betty [Unpaid] Jane [Unpaid]

____________________________________________________________
```
### Generate a simplified debt settlement plan: `split`
Generates and displays the debt settlement plan that uses the least number of transactions for everyone.

Format: `split`

Example of usage: 

```
> split
____________________________________________________________
Best way to settle debts:
Bob pays John $38.10
Betty pays John $15.74
Betty pays Jane $7.79
____________________________________________________________
```
### Mark as "paid" when settled: `paid`

Marks an expense as paid for a specific person.

Format: `paid n/NAME i/IDENTIFIER`

* Note: The `IDENTIFIER` used in the command is with respect to that person specified in the command.
  In other words, the `IDENTIFIER` is the number labelled on the activity in the non-fully paid category
  when you execute `list n/NAME`.

* Note: If the paid command is used on an activity where the specified `NAME` is the payer for the activity,
  it will mark all participants in the activity as paid.

* Note: The `IDENTIFIER` shown in the `LIST` command will change after using the `PAID` command. If you 
  intend to use the `PAID` command again, please run the `LIST` command first to get the updated `IDENTIFIER`.

Example of usage: 

```
> list n/John
____________________________________________________________
Activities which have been fully paid for John:

Activities which have not been fully paid for John:
1.  Desc: lunch
    Payer: Jane
    Amount: $28.00 [Unpaid]
2.  [PAYER] Desc: tickets
    Owed by: Bob [Unpaid] Betty [Unpaid] Jane [Unpaid]

____________________________________________________________
> paid n/John i/1
____________________________________________________________
Marked as paid!
____________________________________________________________
> list n/John
____________________________________________________________
Activities which have been fully paid for John:
1.  Desc: lunch
    Payer: Jane
    Amount: $28.00 [Paid]

Activities which have not been fully paid for John:
1.  [PAYER] Desc: tickets
    Owed by: Bob [Unpaid] Betty [Unpaid] Jane [Unpaid]

____________________________________________________________
```

### Unmark as "paid": `unpaid`

Unmarks an expense as unpaid for a specific person.

Format: `unpaid n/NAME i/IDENTIFIER`

* Note: The `IDENTIFIER` used in the command is with respect to that person specified in the command.
  In other words, the `IDENTIFIER` is the number labelled on the activity in the fully paid category when you execute `list n/NAME`.

* Note: If the unpaid command is used on an activity where the specified `NAME` is the payer for the activity,
  it will mark all participants in the activity as unpaid.

* Note: The `IDENTIFIER` shown in the `LIST` command will change after using the `UNPAID` command. If you
  intend to use the `UNPAID` command again, please run the `LIST` command first to get the updated `IDENTIFIER`.
  
Example of usage: 

```
> list n/Bobby
____________________________________________________________
Activities which have been fully paid for Bobby:
1.  Desc: tickets
    Payer: Johnny
    Amount: $15.00 [Paid]

Activities which have not been fully paid for Bobby:

____________________________________________________________
> unpaid n/Bobby i/1
____________________________________________________________
Marked as unpaid!
____________________________________________________________
> list n/Bobby
____________________________________________________________
Activities which have been fully paid for Bobby:

Activities which have not been fully paid for Bobby:
1.  Desc: tickets
    Payer: Johnny
    Amount: $15.00 [Unpaid]

____________________________________________________________
```

### Edit description of an activity: `edit`

Edit the description of an existing activity.

Format: `edit i/IDENTIFIER d/NEWDESCRIPTION`

Example of usage:

```
> list
____________________________________________________________
You have 2 activities:
1.  Desc: lunch
    Payer: Jane
    Owed by: John
2.  Desc: tickets
    Payer: John
    Owed by: Bob, Betty, Jane
____________________________________________________________
> edit i/1 d/dinner
____________________________________________________________
Description has been changed to dinner
____________________________________________________________
> list
____________________________________________________________
You have 2 activities:
1.  Desc: dinner
    Payer: Jane
    Owed by: John
2.  Desc: tickets
    Payer: John
    Owed by: Bob, Betty, Jane
____________________________________________________________
```

### Edit payer name of an activity: `edit`

Edit the name of a payer for an existing activity.

Format: `edit i/IDENTIFIER n/NEWNAME`

```
> list
____________________________________________________________
You have 1 activities:
1.  Desc: tickets
    Payer: John
    Owed by: Bob, Betty, Jane
____________________________________________________________
> edit i/1 n/Johnny
____________________________________________________________
Payer's name has been modified to Johnny
____________________________________________________________
> list
____________________________________________________________
You have 1 activities:
1.  Desc: tickets
    Payer: Johnny
    Owed by: Bob, Betty, Jane
____________________________________________________________
```

### Edit name of a friend that owes: `edit`

Edit the name of a friend that owes money for an existing activity.

Format: `edit i/IDENTIFIER f/NEWNAME o/OLDNAME`

```
> list
____________________________________________________________
You have 1 activities:
1.  Desc: tickets
    Payer: Johnny
    Owed by: Bob, Betty, Jane
____________________________________________________________
> edit i/1 f/Bobby o/Bob
____________________________________________________________
Bob's name has been changed to Bobby
____________________________________________________________
> list
____________________________________________________________
You have 1 activities:
1.  Desc: tickets
    Payer: Johnny
    Owed by: Betty, Bobby, Jane
____________________________________________________________
```

### Edit amount of a friend that owes: `edit`

Edit the amount of a friend that owes money for an existing activity.

Format: `edit i/IDENTIFIER a/NEWAMOUNT o/FRIENDNAME`

```
> list n/Bobby
____________________________________________________________
Activities which have been fully paid for Bobby:

Activities which have not been fully paid for Bobby:
1.  Desc: tickets
    Payer: Johnny
    Amount: $38.10 [Unpaid]

____________________________________________________________
> edit i/1 a/15 o/Bobby
____________________________________________________________
Bobby's amount owed has been changed to 15
____________________________________________________________
> list n/Bobby
____________________________________________________________
Activities which have been fully paid for Bobby:

Activities which have not been fully paid for Bobby:
1.  Desc: tickets
    Payer: Johnny
    Amount: $15.00 [Unpaid]

____________________________________________________________
```

### Change to group selection menu: `change`
Navigate to the group selection menu.

Format: `change`

Example of usage:
```
____________________________________________________________
Please select a group number from the following...
(You currently have no available groups to load)
... or give your new group a name:
> test
____________________________________________________________
You are currently in the "test" group.
It is a new group.
____________________________________________________________
> change
____________________________________________________________


Welcome to PayPals!
____________________________________________________________
Would you like to delete or select a group?
____________________________________________________________
>
```


### Exiting the application: `exit`
Exits the application.

Format: `exit`

Example of usage: 

```
> exit
____________________________________________________________
Thank you for using PayPals!
Hope you have enjoyed your trip and see you again soon!
____________________________________________________________
```
## Command Tips

- You can use the `list` command without a name to see all activities.
- If you're not sure of an activity's ID, use `list` to find it before using `edit`, `paid`, or `delete`.
- Use `addequal` when everyone owes an equal share – it's quicker and cleaner!
- `split` only calculates who owes whom — it doesn't change any records.
- Use `help` anytime to see all available commands and their expected formats.
- Commands are case-insensitive, so `ADD`, `Add`, and `add` all work the same.
- You can repeat `f/... a/...` pairs in the `add` command to include multiple friends.
- Use `edit` to fix typos or update activity details instead of deleting and re-adding.
- To check what someone still owes, use `list n/NAME` — it shows paid and unpaid sections clearly.
- Use `paid n/PAYER i/IDENTIFIER` to instantly mark all friends in the activity as paid.
- After using `paid` or `unpaid`, run `list` again to get the updated identifiers for future actions.
- If you’re unsure why something failed, scroll down to the [Common Errors](#common-errors) section for examples and explanations.



## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Install PayPals on the other computer and replace the save file in data directory with the save file containing you data.

## Common Errors

### Failure to adhere to command format

PayPals will output the error messages in the event that the user input does not match the corresponding format for the desired operation.
For example:
```
> lsit
____________________________________________________________
INPUT ERROR: Invalid command entered
Try these commands: add | delete | edit | list | split | paid | unpaid | exit | help
____________________________________________________________
```

Or

```
> add d/Dinner f/Bob a/10
____________________________________________________________
Format: add d/DESCRIPTION n/PAYER f/FRIEND1 a/AMOUNT_OWED_1 f/FRIEND2 a/AMOUNT_OWED_2...
INPUT ERROR: No name of payer
____________________________________________________________
```

This could be potentially caused by 
* Misspelled commands (e.g. lsit instead of list)
* Absence of required parameters for the command
* Absence of parameter prefixes (e.g. n/)
* User input does not follow command format

### Invalid Requests

As PayPals is designed to specifically target real-life transactions, illogical requests going against real-life standards
may also trigger error messages. For example:

```
> list n/John
____________________________________________________________
Activities which have been fully paid for John:
1.  Desc: lunch
    Payer: Jane
    Amount: $28.00 [Paid]

Activities which have not been fully paid for John:

____________________________________________________________
> edit i/1 a/10 o/John
____________________________________________________________
ERROR: Unable to edit amount owed if it has been paid
____________________________________________________________
```

Or

```
> add d/lunch n/Jane f/John a/18.274517
____________________________________________________________
INPUT ERROR: Please enter amounts up to 2 decimal places.
____________________________________________________________
> 
```

Or

```
> add d/lunch n/Jane f/John a/999999
____________________________________________________________
INPUT ERROR: Too large amount entered exceeding limit
____________________________________________________________
> 
```

This could potentially be caused by
* Invalid parameters (Monetary value more than 2 decimal places)
* Illogical parameters (Changing the amount owed after it has been paid, or owing tens of thousands of dollars)

## Known Issues

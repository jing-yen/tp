# PayPals User Guide

## Table of Contents
- [PayPals User Guide](#paypals-user-guide)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Quick Start](#quick-start)
  - [Command Reference](#command-reference)
  - [Features](#features)
    - [Expense Tracking](#expense-tracking)
    - [Debt Settlement](#debt-settlement)
    - [Storage](#storage)
  - [Command Format](#command-format)
    - [Viewing help: `help`](#viewing-help-help)
    - [Adding an expense with details: `add`](#adding-an-expense-with-details-add)
    - [Delete an expense: `delete`](#delete-an-expense-delete)
    - [List all past expenses: `list`](#list-all-past-expenses-list)
    - [Generate a simplified debt settlement plan: `split`](#generate-a-simplified-debt-settlement-plan-split)
    - [Mark as "paid" when settled: `paid`](#mark-as-paid-when-settled-paid)
    - [Edit description of an activity: `edit`](#edit-description-of-an-activity-edit)
    - [Edit payer name of an activity: `edit`](#edit-payer-name-of-an-activity-edit)
    - [Edit name of a friend that owes: `edit`](#edit-name-of-a-friend-that-owes-edit)
    - [Edit amount of a friend that owes: `edit`](#edit-amount-of-a-friend-that-owes-edit)
    - [Exiting the application: `exit`](#exiting-the-application-exit)
  - [FAQ](#faq)
  - [Common Errors](#common-errors)
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
5. Type the command in the command box and press Enter to execute it. e.g. typing help and pressing Enter will open the help window.
6. You can now start using PayPals!

## Command Reference

A quick reference table for all commands is presented below.

| Task                                        | Command Expression                                                    |
|---------------------------------------------|-----------------------------------------------------------------------|
| Help menu                                   | `help`                                                                |
| Add an activity                             | `add d/DESCRIPTION n/NAME f/FRIEND1 a/AMOUNT1 f/FRIEND2 a/AMOUNT2...` |
| Delete an activity                          | `delete i/IDENTIFIER`                                                 |
| List all expenses                           | `list`                                                                |
| List all expenses by a person               | `list n/NAME`                                                         |
| Split bills                                 | `split`                                                               |
| Mark an activity as paid for a person       | `paid n/NAME i/IDENTIFIER`                                            |
| Mark an activity as unpaid for a person     | `unpaid n/NAME i/IDENTIFIER`                                          |
| Mark an activity as paid for everyone       | `paid n/PAYER i/IDENTIFIER`                                           |
| Edit the description of an activity         | `edit i/IDENTIFIER d/DESCRIPTION`                                     |
| Edit the payer name of an activity          | `edit i/IDENTIFIER n/NEWNAME`                                         |
| Edit the name of a friend that owes money   | `edit i/IDENTIFIER f/NEWNAME o/OLDNAME`                               |
| Edit the amount of a friend that owes money | `edit i/IDENTIFIER a/NEWAMOUNT o/FRIENDNAME`                          |
| Close the application                       | `exit`                                                                |

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

## Command Format

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
  2. delete -> delete i/IDENTIFIER
  3. paid   -> paid n/NAME i/IDENTIFIER
  4. unpaid -> unpaid n/NAME i/IDENTIFIER
  5. edit   -> (Description) edit i/IDENTIFIER d/NEWDESCRIPTION
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
### Delete an expense: `delete`
Deletes an expense.

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
  In other words, the `IDENTIFIER` is the number labelled on the activity when you execute `paid n/NAME`.

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
  In other words, the `IDENTIFIER` is the number labelled on the activity when you execute `paid n/NAME`.

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

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Install PayPals on the other computer and replace the save file in data directory with the save file containing you data.

## Common Errors

## Known Issues

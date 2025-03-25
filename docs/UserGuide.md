# PayPals User Guide

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

|Task|Command Expression|
|----|------------------|
|Help menu|`help`|
|Add an acitivity|`add d/DESCRIPTION n/NAME f/FRIEND1 a/AMOUNT1 f/FRIEND2 a/AMOUNT2...`|
|Delete an activity|`delete i/IDENTIFIER`|
|List all expenses|`list`|
|List all expenses by a person|`list n/NAME`|
|Split bills|`split`|
|Mark an activity as paid for a person|`paid n/NAME i/IDENTIFIER`|
|Mark an activity as paid for everyone|`paid n/PAYER i/IDENTIFIER`|
|Close the application|`exit`|

## Table of Contents
- [PayPals User Guide](#paypals-user-guide)
  - [Introduction](#introduction)
  - [Quick Start](#quick-start)
  - [Command Reference](#command-reference)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
    - [Expense Tracking](#expense-tracking)
    - [Debt Settlement](#debt-settlement)
    - [Storage](#storage)
  - [Command Format](#command-format)
    - [Adding an expense with details: `add`](#adding-an-expense-with-details-add)
    - [Delete an expense: `delete`](#delete-an-expense-delete)
    - [List all past expenses: `list`](#list-all-past-expenses-list)
    - [Generate a simplified debt settlement plan: `split`](#generate-a-simplified-debt-settlement-plan-split)
    - [Mark as "paid" when settled: `paid`](#mark-as-paid-when-settled-paid)
    - [Exiting the application: `exit`](#exiting-the-application-exit)
  - [FAQ](#faq)

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

### Adding an expense with details: `add`
Adds an expense.

Format: `add d/DESCRIPTION n/NAME f/FRIEND1 a/AMOUNT1 f/FRIEND2 a/AMOUNT2...`

Example of usage: 

```
add d/lunch n/Jane f/John a/28
    Desc: lunch
    Name of payer: Jane
    Number of friends who owe Jane: 1
```

```
add d/tickets n/John f/Betty a/23.53 f/Jane a/20.21 f/Bob a/38.10
    Desc: tickets
    Name of payer: John
    Number of friends who owe John: 3
```
### Delete an expense: `delete`
Deletes an expense.

Format: `delete i/IDENTIFIER`

Example of usage: 

```
delete i/2
    Expense removed successfully!
```
### List all past expenses: `list`
Displays all past expenses for everyone or a single person.

Format: `list [n/NAME]`

* If no name is provided, all past expenses for everyone are displayed.

Example of usage: 

```
list
 1. Desc: tickets
    Payer: John
    Owed by: Bob, Betty, Jane
    Desc: lunch
    Payer: Jane
    Owed by: John
```
```
list n/Sean
    Sean: 
      1.  [PAYER] Desc: tickets
          Owed by: Bob [Paid] Betty [Paid] Jane [Paid]
      2.  Desc: lunch
          Payer: Jane
          Amount: $28.00 [Unpaid]
```
### Generate a simplified debt settlement plan: `split`
Generates and displays the debt settlement plan that uses the least number of transactions for everyone.

Format: `split`

Example of usage: 

```
Split
    Best way to settle debts:
    Bob pays Sean $2.0
    Bob pays Brandon $2.0
```
### Mark as "paid" when settled: `paid`
Marks an expense as paid for a specific person.

Format: `paid n/NAME i/IDENTIFIER`

Example of usage: 

```
paid n/John Doe i/2
    Marked as paid!
```
### Exiting the application: `exit`
Exits the application.

Format: `exit`

Example of usage: 

```
exit
    Goodbye! Hope you have enjoyed your trip and see you again soon!
```

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: Install PayPals on the other computer and replace the save file in data directory with the save file containing you data.

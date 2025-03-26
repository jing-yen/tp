# Developer Guide
## Table of Contents
- [Acknowledgements](#acknowledgements)
- [Design](#design)
  - [Architecture](#architecture)
  - [UI component]()
  - [Logic component]()
  - [Model component]()
  - [Storage component]()
  - [Common classes]()
- [Implementation](#implementation)
- [Documentation, logging, testing, configuration, dev-ops]()
- [Appendix: Requirements](#appendix-requirements)
  - [Product scope](#product-scope)
  - [User stories](#user-stories)
  - [Use cases](#use-cases)
  - [Non-functional Requirements](#non-functional-requirements)
  - [Glossary](#glossary)
- [Appendix: Instructions for Testing](#instructions-for-testing)
  - [Deleting a person]()
  - [Saving data]()

## Acknowledgements

PayPals uses the following tools for development and testing:

1. [JUnit 5](https://junit.org/junit5/) - Used for software testing.
2. [Gradle](https://gradle.org) - Used for build automation.

## Design
### Architecture

(edit in docs/diagram/MainArchitecture.puml)
The **Architecture Diagram** given above explains the high-level design of the App.  
  
Given below is a quick overview of the main components and how they interact with each other.  
  
**Main components of the architecture**  
` Paypals` is in charge of the app launch and shut down.  
- At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
- At shut down, it shuts down the other components and invokes cleanup methods where necessary (?)
  
The bulk of the app's work is down by the following five components:
* `Ui`: The UI of the App.
* `Parser`: Parse user entry as commands
* `Command`: The command executor
* `Activity`: Holds the data of the App in memory
* `Storage`: Reads data from, and writes data to, the hard disk.
     
**How the architecture components interact with each other**  
The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command ``  
  
  
### UI component

### Parser component

### Command component

### Activity component

### Storage component

## Implementation

## Appendix: Requirements
## Product scope
### Target user profile:
- has a need to manage expenses when travelling with friends
- prefer desktop apps over other types
- can type fast
- prefers typing to mouse interactions
- is reasonably comfortable with CLI apps

This product is for university students who frequently travel with friends.

### Value proposition

The product aims to provide assistance in simplifying payments by minimising the number of transactions needed to balance expenses.

## User Stories

| Version | As a ...                          | I can ...                                                         | So that I ...                                               |
|---------|-----------------------------------|-------------------------------------------------------------------|-------------------------------------------------------------|
| v1.0    | student                           | add an expense and specify who paid and who owes                  | can keep track of spending                                  |
| v1.0    | student who paid for a whole meal | split expenses                                                    | can fairly divide shared costs based on consumption         |
| v1.0    | user                              | delete an expense                                                 | can get rid of an incorrect entry                           |
| v1.0    | user                              | mark expenses as settled or unsettled                             | can keep track of cleared debts                             |
| v1.0    | user                              | view the expenses                                                 | know how much I owe or how much someone owes me             |
| v1.0    | user                              | save current expenses when I exit the application                 | can view them again when I run the application another time |
| v2.0    | new user                          | use a command to display all the common commands and their syntax | know how to use the app                                     |

## Use cases
(For all use cases below, the **System** is `Paypals` and the **Actor** is the `user`, unless specified otherwise)
### Use case: Delete an expense  
  
**MSS**
1. User requests to list expenses
2. Paypals shows a list of expenses
3. User requests to delete a specific expense in the list
4. Paypals deletes the expense
  
&ensp; &ensp; Use case ends  
  
**Extensions**
- 2a. The list is empty.  
    Use case ends.
- 3a. The given index is invalid.
  - 3a1. Paypals shows an error message.  
    Use case resumes at step 2.

## Non-Functional Requirements
1. Should work on any mainstream OS as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000(?) expenses without a noticeable sluggishness in performance for typical usage
3. A user with above average typing speed for regular English test (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. (add more)

## Glossary

* *Mainstream OS* - Windows, Linux, Unix, macOS
* Payer - The person that has paid for a specific activity for the group.
* Friend - A friend that is part of an activity that owes money to the payer.

## Instructions for testing
* Technical Requirements: Any mainstream OS, i.e. Windows, macOS or Linux, with Java 17 installed. Instructions for downloading Java 17 can be found [here](https://www.oracle.com/java/technologies/downloads/#java17).
* Project Scope Constraints: Data storage is only to be performed locally.
* Quality Requirements: The application should be intuitive and easy to use by a beginner with little to no experience with CLIs.

### Manual Testing

Please view the [User Guide](UserGuide.md) for the full list of UI commands, their related use cases and the expected outcomes.

### JUnit Testing
JUnit tests are written in the [`test directory`](../src/test/java/paypals) and serve to test key methods part of the application.

# Developer Guide
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
- [Appendix: Instructions for manual testing](#appendix-instructions-for-manual-testing)
  - [Launch and shutdown]()
  - [Deleting a person]()
  - [Saving data]()

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

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

### Value proposition :
provide assistance in simplifying payments by minimising the number of transactions needed to balance expenses.  


## User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Version | Priority | As a ... | I want to ...                                 | So that I can ...                               |
|---------|----------|----------|-----------------------------------------------|-------------------------------------------------|
| v1.0    | `* * *`  | user     | add an expense and specify who owes           | keep track of spending                          |
| v1.0    | `* * *`  | user     | split expenses                                | fairly divide shared costs                      |
| v1.0    | `* * *`  | user     | delete an expense                             | get rid of an incorrect entry                   |
| v1.0    | `* * *`  | user     | mark expenses as paid                         | keep track of cleared debts                     |
| v1.0    | `* * *`  | user     | view the expenses                             | know how much I owe or how much someone owes me |
| v2.0    | `* *`    | new user | view all the common commands and their syntax | know how to use the app                         |

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

## Appendix: Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}

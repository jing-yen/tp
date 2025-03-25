# Developer Guide

## Table of Contents
- [Developer Guide](#developer-guide)
  - [Table of Contents](#table-of-contents)
  - [Acknowledgements](#acknowledgements)
  - [Design \& Implementation](#design--implementation)
  - [Product scope](#product-scope)
    - [Target user profile](#target-user-profile)
    - [Value proposition](#value-proposition)
  - [User Stories](#user-stories)
  - [Non-Functional Requirements](#non-functional-requirements)
  - [Glossary](#glossary)
  - [Instructions for Testing](#instructions-for-testing)
    - [Manual Testing](#manual-testing)
    - [JUnit Testing](#junit-testing)

## Acknowledgements

PayPals uses the following tools for development and testing:

1. [JUnit 5](https://junit.org/junit5/) - Used for software testing.
2. [Gradle](https://gradle.org) - Used for build automation.

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}


## Product scope
### Target user profile

This product is for university students who frequently travel with friends.

### Value proposition

The product aims to provide assistance in simplifying payments by minimising the number of transactions needed to balance expenses.

## User Stories

|Version| As a ... | I can ... | So that I ...|
|--------|----------|---------------|------------------|
|v1.0|student|add an expense and specify who paid and who owes|can keep track of spending|
|v1.0|student who paid for a whole meal|split expenses|can fairly divide shared costs based on consumption|
|v1.0|user|delete an expense|can get rid of an incorrect entry|
|v1.0|user|mark expenses as settled or unsettled|can keep track of cleared debts|
|v1.0|user|view the expenses|know how much I owe or how much someone owes me|
|v1.0|user|save current expenses when I exit the application|can view them again when I run the application another time|

## Non-Functional Requirements

* Technical Requirements: Any mainstream OS, i.e. Windows, macOS or Linux, with Java 17 installed. Instructions for downloading Java 17 can be found [here](https://www.oracle.com/java/technologies/downloads/#java17).
* Project Scope Constraints: Data storage is only to be performed locally.
* Quality Requirements: The application should be intuitive and easy to use by a beginner with little to no experience with CLIs.

## Glossary

* Payer - The person that has paid for a specific acitivity for the group.
* Friend - A friend that is part of an activity that owes money to the payer.

## Instructions for testing

### Manual Testing

Please view the [User Guide](UserGuide.md) for the full list of UI commands, their related use cases and the expected outcomes.

### JUnit Testing

JUnit tests are written in the [`test directory`](../src/test/java/paypals) and serve to test key methods part of the application.

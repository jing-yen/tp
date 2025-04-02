# Neo Yin Qi (icknee) - Project Portfolio Page

## Overview
PayPals is a CLI-based application that aims to provide assistance in simplifying payments by minimising the number of transactions needed to balance expenses.

### Summary of Contributions

Given below are my contributions to the project.

* **New Feature**: Implemented Delete Command
  * What it does: Allows the user to delete expenses one at a time from the activity manager.
  * Justification: This feature lets the user delete an expense if it is written wrongly, already paid for, or does not want it to be tracked. Without this functionality, users would be unable to correct mistakes or remove outdated expenses, leading to inaccurate financial records.
  * Highlights: This feature requires in-depth analysis of how the data is stored such that all data that has to be modified is well taken care of. The implementation includes proper error handling to prevent invalid deletions and maintains data integrity by updating all related records when an expense is removed.


* **New Feature**: Implemented Add Expense with Equal Portions
  * What it does: Allows the user to add an expense where the amount is split equally among everyone in the group.
  * Justification: This reduces the need for the user to manually calculate the expense and input every amount using the custom add function. For common group expenses like meals or shared utilities, this significantly streamlines the data entry process.
  * Highlights: This enhancement inherits from the add command, making use of code from the parent class. This approach promotes code reusability and maintainability while extending the functionality of the basic add command.
  

* **Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=icknee&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)


* **Documentation**
  * User Guide
    * Added comprehensive documentation for the feature `addequal`, including command syntax, parameter explanations, and usage examples to help users understand how to effectively use the equal splitting functionality.
    * Made tweaks to existing documentation of the feature `help` to improve clarity and provide more detailed information about available commands.
  * Developer Guide
    * Created diagram for the architecture, illustrating the high-level design of the application and the relationships between major components.
    * Created sequence diagram for execute method in delete command, showing the interaction between objects when a delete operation is performed, which helps other developers understand the flow of execution.
    * Wrote brief summary of the main components of the architecture, explaining the purpose and responsibilities of each component in the system.
    * Wrote non-functional requirements section detailing performance expectations, reliability standards, and compatibility requirements for the application.


* **Project Management**
  * Managed release v1.0 on GitHub, creating the release with appropriate version tags.
  * Wrote some of the issues in the issue tracker to document bugs, feature requests, and enhancement ideas, helping to organize the development workflow.
  * Enabled assertions in the codebase.


* **Community**
  * Gave PR comments to encourage group members, maintaining a positive team environment.
  * Helped teammates troubleshoot issues with implementation.

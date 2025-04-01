# Sean Ter (SeanTerrr) - Project Portfolio Page

## Overview
PayPals is a CLI-based application that aims to provide assistance in simplifying payments by minimising the number of transactions needed to balance expenses.


### Summary of Contributions

Given below are my contributions to the project.

* **Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=SeanTerrr&breakdown=true)


* **New Feature**: Implemented Split Command
    * What it does: Calculates the optimal way to settle group expenses by generating 
      the minimum number of transactions between members.
    * Justification: This is the core functionality of Paypals. 
    * Highlights: Implements a greedy debt minimization algorithm to ensure that everyone 
      pays with the minimal number of transactions.


* **New Feature**: Implemented List Command
    * What it does: Displays the list of all recorded expenses within a group, allowing users to track spending and identify who owes whom.
    * Justification: List command helps users stay informed about their financial activity within the group.
    * Highlights:  Supports two display modes:
      * A summary of the group expensess.
      * A detailed view for a specific person, showing whether each activity is fully paid or unpaid.


* **New Feature**: Implemented Paid Command
    * What it does: Allows users to manually mark a person’s share in an activity as paid.
    * Justification: Paid Command is necessary as it would allow users to mark an expense as paid if the user would like the pay the person
      individually without splitting.
    * Highlights: Works for both payers and payees. If the person is a payer, the command will mark all contributions in the activity as paid.


* **New Feature**: Implemented Unpaid Command
    * What it does: Allows users to reverse a payment by marking a previously paid expense as unpaid.
    * Justification: Provides flexibility in correcting accidental inputs or reverting payment statuses.
    * Highlights: Similar to the Paid Command, this applies to both payers and payees. For payers, it marks the entire activity as unpaid.


* **New Feature**: Implemented Change Command
    * What it does: Enables users to switch between different groups of friends.
    * Justification:  Essential for users managing multiple groups (e.g., family, friends, roommates) with separate expenses.
    * Highlights: Streamlines the process by allowing group switching directly within the CLI, eliminating the need to restart the application.


* **Enhancement to Existing Features**:
    * List Command now split the activities into 2 categories, fully paid and non fully paid. (Pull Request [#102](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/102))


* **Documentation**
    * User Guide
        * Added documentation for the feature `change`.
        * Added additional documentation to `list`, `paid`, `unpaid` and `split` command to increase reader's clarity.

    
* **Community**
    * Pull Requests reviewed: [#100](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/100), [#113](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/113)
# Neo Yin Qi (icknee) - Project Portfolio Page

## Overview
Paypals is a desktop application used for tracking expenses in groups. The user interacts with it using a CLI. It is written in Java, and has about 3 kLoC.


## Summary of Contributions
* ### Code contributed
  * [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=icknee&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2025-02-21&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)
* ### Feature 1: Delete commands
  * What it does: allows the user to delete expenses one at a time.
  * Justification: This feature lets the user delete an expense if it is written wrongly, already paid for, or does not want it to be tracked.
  * Highlights: This feature requires in-depth analysis of how the data is stored such that all data that has to be modified is well taken care of.
* ### Feature 2: Add expense with equal portions of spending
  * What it does: allows the user to add an expense where the amount is split equally among everyone
  * Justification: This reduces the need for the user to manually calculate the expense and input every amount using the custom add function
  * Highlights: This enhancement inherits from the add command, making use of code from the parent class.
* ### Contributions to the UG
  * Added documentation for the feature `addequal`
  * Did tweaks to existing documentation of features `help`
* ### Contributions to the DG
  * Made diagram for the architecture
  * Wrote brief summary of the main components of the architecture
  * Wrote non-functional requirements
* ### Contributions to team-based tasks
  * Managed release v1.0 on GitHub
  * Wrote some of the issues
  * Enabled assertions
* ### Review/mentoring contributions
  * Gave PR comments to encourage group members 

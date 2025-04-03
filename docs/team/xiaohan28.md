# xiaohan28's Project Portfolio Page

## Overview
PayPals is a CLI-based application that aims to simplify payments by minimising the number of transactions needed to balance shared expenses.

## Summary of Contributions

Given below are my contributions to the project.

* **New Feature: Implemented Help Command**
    * **What it does**: Enhanced the Help command to provide detailed usage instructions and improved overall formatting.
    * **Justification**: A clear Help command reduces the learning curve for users and minimizes common input errors.
    * **Highlights**: The updated Help command now includes comprehensive examples and guidance on using all available commands.


* **New Feature: List Balance Functionality**
    * **What it does**: Implemented the `list balance n/NAME` command to display a user's net balance, summarising all unpaid transactions into a single positive or negative amount.
    * **Justification**: This feature provides users with a quick snapshot of their financial standing within a group, helping them manage expenses efficiently.
    * **Highlights**: Robust balance calculation logic that accounts for both amounts owed to and by the user.


* **General Code Contributions: Refactoring, Formatting and Logging**
  * **What it does**:
    - Improved the readability and output formatting of the list function so that activities are displayed with clear, properly indented details. (Pull Request [#124](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/124))
    - Refactored the AddCommand to enhance overall code readability and maintainability. (Pull Request [#47](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/47))
    - Enhanced logging practices across the codebase to provide consistent, detailed runtime diagnostics.
  * **Justification**:
    - Clearer output formatting in the list function makes it easier for users to understand the status of their transactions.
    - Refactoring the AddCommand contributes to cleaner, more structured code that is easier to maintain and debug.
    - Improved logging simplifies troubleshooting and supports graceful error recovery.
  * **Highlights**:
    - Refined list function with better indentation and clearer separation of paid and unpaid activities.
    - Restructured AddCommand for improved code clarity and maintainability.
    - Consolidated logging across modules to ensure robust error reporting.


* **General Enhancements: Documentation & UI Improvements**
  * **What it does**:
    - Added comprehensive Javadoc comments to key classes (e.g., `AddCommand`, `HelpCommand`, `EditCommand`) to improve code readability and maintainability. (Pull Request [#131](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/131))
    - Refined the UI class to standardise output formatting and ensure consistent display across commands. 
  * **Justification**:
    - Clear and comprehensive documentation is vital for long-term maintenance and for new developers to quickly understand the codebase.
    - A consistent, clear UI greatly enhances the user experience, especially in a CLI application.
  * **Highlights**:
    - Streamlined and standardised documentation across the project, facilitating debugging and future feature enhancements.
    - Updated display formats for command responses and enhanced error messaging in the UI class.


* **User Guide Improvements**
    * **What it does**: Revised all command examples to reflect clearer outputs, added a detailed Help section, included a Glossary to define key terms, and incorporated Command Tips to assist users. (Pull Request [#122](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/122))
    * **Justification**: Enhancing the User Guide ensures that new users can quickly understand and effectively use the application.
    * **Highlights**: Added the new `list balance` information and improved troubleshooting guidance for command mistakes.


* **Developer Guide Enhancements**
    * **What it does**: Added sequence diagrams that illustrate interactions among key components (commands, storage, UI, etc.) (Pull Request [#117](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/117)) and a class diagram for the activity component (showing interactions between `Activity`, `ActivityManager`, `Person`, and `Logging`). (Pull Request [#94](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/94))
    * **Justification**: Detailed diagrams provide clarity on system architecture, making it easier for future development and maintenance.
    * **Highlights**: Comprehensive coverage of the activity component section and clear documentation of internal workflows.


* **Project Management & Community Contributions**
    * Participated actively in code reviews and provided constructive feedback.
    * Assisted in updating both the User Guide and Developer Guide.
    * Review of pull requests. (Pull Request [#135](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/135), [#134](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/134), [#130](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/130), [#110](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/110), [#107](https://github.com/AY2425S2-CS2113-T13-2/tp/pull/107))


* **Code Contributed**: [RepoSense Link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=xiaohan28&breakdown=true)

---

This PPP reflects my contributions in enhancing documentation, improving both user and developer guides, and refining the user interface—all of which have increased the quality, maintainability, and usability of PayPals.

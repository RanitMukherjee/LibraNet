# LibraNet

## Scenario

LibraNet is an online library platform managing books, audiobooks, and e-magazines. Each item type shares common attributes like title, author, and availability status, but differs in behavior: books have page counts, audiobooks have playback durations, and e-magazines have issue numbers. The system tracks borrowing, calculates fines, and supports searching by item type.


### Task

Design a Java (use your preferred language) class structure for reusability, extensibility, and robust data handling, addressing:
- Common Operations: Borrowing, returning, and checking availability. Fines are numeric (e.g., 10 rs/day) and stored in collections.
- Specialized Behaviors: Books need getPageCount(); audiobooks implement a Playable interface; e-magazines need archiveIssue().

- Data Handling: Parse borrowing durations (strings) and store item IDs as integers. Make sure errors are properly handled.

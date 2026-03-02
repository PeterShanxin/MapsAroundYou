# Team Coding Quality Guidelines (Java)

## 0. Purpose

This document defines the minimum coding quality bar for our team project.

**Baseline:** We follow the SE-EDU Java Coding Standard (basic + intermediate) for day-to-day coding. For anything not covered, default to the Google Java Style Guide.

**Rule of thumb:** Prefer clarity over cleverness. Code should be easy to read, review, test, and change.

---

## 1. Non-negotiables

* Code merged to `main` must compile and pass tests.
* Follow the naming and formatting rules in this document.
* No “temporary hacks” without a linked issue and a clear removal plan.

---

## 2. Coding Standard (baseline rules)

### 2.1 Naming

* **Packages:** all lowercase. For school projects, use a root package based on your project/group name (do not use `edu.nus.comp.*`).
* **Classes / enums:** PascalCase nouns, e.g., `RoutePlanner`, `UserRole`.
* **Methods:** camelCase verbs, e.g., `computeTotalCost()`.
* **Variables:** camelCase.
* **Constants:** SCREAMING_SNAKE_CASE, e.g., `MAX_RETRIES`.
* **Booleans:** name to sound boolean. Prefer prefixes like `is`, `has`, `can`, `should`, `was`.

  * Examples: `isValid`, `hasAccess`, `shouldAbort`.
  * Boolean setters: `setFound(boolean isFound)`.
* **Collections:** use plural nouns, e.g., `List<Book> books`.
* **Acronyms:** do not fully uppercase inside names.

  * Good: `openDvdPlayer()`
  * Bad: `openDVDPlayer()`
* **Test method names:** underscores are allowed using `featureUnderTest_testScenario_expectedBehavior()`.

### 2.2 Layout and formatting

* **Indentation:** 4 spaces, not tabs.
* **Line length:** soft limit 110 chars, hard limit 120 chars.
* **Wrapped lines:** indent continuation lines by +8 spaces relative to the parent line.
* **Line wrapping goals:** improve readability. Do not blindly accept IDE auto-wrap.

  * Break after commas.
  * Break before operators (including `.` in chained calls).
  * Keep a method/constructor name attached to the following `(`.

### 2.3 Braces and control structures

* Use **K&R (Egyptian) braces**.
* **Always use braces** for `if`, `else`, `for`, `while`, `do-while` blocks, even for single statements.
* `switch`:

  * Keep `case` labels aligned with `switch`.
  * If you intentionally omit a `break`, add `// Fallthrough`.

### 2.4 Packages and imports

* Every class should be in a **package** (avoid the unnamed package).
* Avoid wildcard imports (e.g., `import java.util.*;`). Import only what you use.
* Keep import ordering consistent across the project. Let IDE organize imports.

### 2.5 Types and variables

* Array brackets belong to the **type**, not the variable name.

  * Prefer `int[] values`, not `int values[]`.
* Declare variables in the **smallest possible scope** and initialize near declaration.

### 2.6 Comments and Javadoc

* Write comments in **English**.
* Public classes and public methods should have **Javadoc** where it improves understanding.

  * Include `@param`, `@return`, `@throws` when relevant.
* Comments must align with surrounding indentation.
* Prefer **self-explanatory code** over excessive comments.

### 2.7 Class member ordering (consistency)

Within a class/interface, keep elements in a predictable order:

1. Class/interface Javadoc
2. Class/interface declaration
3. Static fields (public -> protected -> package -> private)
4. Instance fields (public -> protected -> package -> private)
5. Constructors
6. Methods

---

## 3. Readability and maintainability rules (code quality)

### 3.1 Avoid magic literals

* Do not hardcode “mystery numbers/strings” in logic.
* Replace them with:

  * `static final` constants
  * `enum` values
  * configuration constants

### 3.2 Keep methods short and cohesive

* If a method grows beyond ~30 LoC, consider refactoring.
* Prefer extracting well-named helper methods.
* Each method should do **one logical thing**.

### 3.3 Avoid deep nesting

* Deep nesting increases cognitive load.
* Techniques:

  * guard clauses (early returns)
  * extract nested logic into helper methods
  * invert conditions to make the main path unindented

### 3.4 Avoid complicated expressions

* If an expression needs “mental parsing”, split it.
* Use intermediate variables with meaningful names, especially for boolean logic.

### 3.5 Make the code obvious

* Prefer explicit braces and clear formatting.
* Prefer descriptive names over clever tricks.
* If a reviewer needs to ask “what does this do?”, refactor.

### 3.6 Structure code logically

* Group related statements together.
* Keep a consistent narrative flow: setup -> validate -> main logic -> cleanup.

### 3.7 Practice KISS

* Do not try to write “clever” code.
* Prefer the simplest implementation that is correct and readable.

### 3.8 Avoid preemptive optimization

* Optimize only after:

  * the code is correct
  * there is evidence of a bottleneck
* Performance improvements must not significantly reduce readability without justification.

### 3.9 SLAP - Single Level of Abstraction Principle

* Avoid mixing high-level orchestration with low-level calculations in the same block.
* Good pattern:

  * `readData();` -> `processData();` -> `displayResult();`

### 3.10 Make the happy path prominent

* Separate unusual/error cases early.
* Keep the “main flow” unindented where possible.

---

## 4. Assertions and defensive checks

### 4.1 When to use `assert`

Use Java assertions to validate assumptions during development/testing:

* preconditions and postconditions
* invariants
* sanity checks for internal logic

Notes:

* Assertions may be disabled in production. Do not rely on them for user-facing validation.

### 4.2 Exceptions

* Use exceptions for unusual but possible runtime situations.
* Do not swallow exceptions silently.
* Exception messages should be actionable.

---

## 5. Lambdas and Streams (use for clarity, not for style points)

* Use Streams when they make the code **more declarative and easier to read**.
* Avoid long, dense stream pipelines.

  * If a pipeline is getting long, extract parts into named methods.
* Avoid side effects inside streams (`forEach` that mutates external state).
* Prefer loops when control flow is complex (multiple exits, error handling, multi-step state updates).

---

## 6. Quality gates (how we keep standards consistent)

### 6.1 Pull request review checklist

Reviewers should check:

* Naming and formatting compliance
* Readability (nesting depth, method size, obviousness)
* Correctness and edge cases
* Tests added or updated
* No unnecessary complexity or premature optimization

### 6.2 Static analysis

* Use IDE inspections.
* Add linters in CI where possible (examples: Checkstyle, SpotBugs, PMD).

### 6.3 Automated tests

* Add unit tests for core logic.
* Keep unit tests isolated from external dependencies (use stubs/mocks when appropriate).

---

## 7. References

* SE-EDU Java Coding Standard (all rules): `https://se-education.org/guides/conventions/java/index.html`
* SE-EDU Java Coding Standard (basic + intermediate): `https://se-education.org/guides/conventions/java/intermediate.html`
* CS2103DE Lecture 5 slides: `L05.pdf` (Code Quality section roughly pages 41-60; Assertions pages 58-60; Streams pages 61-75)
* CS2103DE Tutorial 1 materials: `Tutorial - 01.pdf` and `T01 Slides.pdf` (coding standard violations and relation to code quality)
* CS2103DE Week 4 lecture slides: `L04.pdf` (code review and static analysis)

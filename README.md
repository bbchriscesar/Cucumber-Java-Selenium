# 🥒 Cucumber-Java-Selenium Test Automation Framework

A BDD (Behavior-Driven Development) test automation framework built with **Cucumber 7**, **Selenium 4**, and **JUnit 5**, targeting [the-internet.herokuapp.com](https://the-internet.herokuapp.com).

---

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Initial Setup](#initial-setup)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Test Scenarios](#test-scenarios)
- [Framework Architecture](#framework-architecture)

---

## Prerequisites

Before running the tests, ensure the following are installed on your machine:

| Tool            | Version  | Verify Command         | Purpose                           |
|-----------------|----------|------------------------|-----------------------------------|
| **Java JDK**    | 21 (LTS) | `java -version`        | Runtime & compilation             |
| **Apache Maven** | 3.8+    | `mvn -version`         | Build & dependency management     |
| **Google Chrome**| Latest   | `google-chrome --version` | Browser under test             |

> **Note:** ChromeDriver is **not** required to be installed manually. Selenium 4's built-in **Selenium Manager** automatically downloads and manages the correct ChromeDriver binary for your Chrome version.

---

## Tech Stack

| Technology       | Version | Role                                                  |
|------------------|---------|-------------------------------------------------------|
| Java             | 21      | Programming language                                  |
| Selenium         | 4.27.0  | Browser automation (WebDriver)                        |
| Cucumber         | 7.20.1  | BDD framework — Gherkin feature files → executable tests |
| JUnit 5          | 5.11.4  | Test platform & runner                                |
| PicoContainer    | 7.20.1  | Lightweight Dependency Injection for Cucumber          |
| Lombok           | 1.18.36 | Compile-time boilerplate code generation              |
| ExtentReports    | 5.1.2   | Rich HTML test reporting                              |
| SLF4J + Logback  | 2.0.16  | Logging facade + implementation                       |

---

## Project Structure

```
Cucumber-Java-Selenium/
├── pom.xml                                         # Maven project config (dependencies, plugins)
├── README.md
└── src/
    └── test/
        ├── java/io/cucumber/
        │   ├── RunCucumberTest.java                 # JUnit 5 Suite runner (entry point)
        │   ├── core/
        │   │   ├── Context.java                     # Facade — shared test context for step classes
        │   │   ├── Manager.java                     # Manages WebDriver & test data stash
        │   │   └── Hooks.java                       # Cucumber hooks (@Before, @After, screenshots)
        │   ├── glue/
        │   │   ├── Navigation.java                  # Shared navigation step definitions
        │   │   ├── HomeSteps.java                   # Homepage verification steps
        │   │   ├── BasicAuthSteps.java              # Basic Auth scenario steps
        │   │   └── SortableDataTablesSteps.java     # Sortable Data Tables scenario steps
        │   └── pages/
        │       ├── BasePage.java                    # Abstract base page (common actions & waits)
        │       ├── HomePage.java                    # Homepage Page Object
        │       ├── BasicAuthPage.java               # Basic Auth Page Object
        │       └── SortableDataTablesPage.java      # Sortable Data Tables Page Object
        └── resources/
            ├── logback-test.xml                     # Logging configuration
            └── io/cucumber/features/
                └── theInternet.feature              # Gherkin feature file (3 test scenarios)
```

---

## Initial Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Cucumber-Java-Selenium
```

### 2. Verify Java & Maven

```bash
# Ensure Java 21 is installed and active
java -version
# Expected: openjdk version "21.x.x" or similar

# Ensure Maven is installed
mvn -version
# Expected: Apache Maven 3.8.x+ with Java 21
```

### 3. Install Dependencies

Maven will automatically download all dependencies declared in `pom.xml`:

```bash
mvn clean install -DskipTests
```

This downloads and caches all libraries (Selenium, Cucumber, JUnit, etc.) into your local Maven repository (`~/.m2/repository`).

### 4. IDE Setup (Optional)

If using **IntelliJ IDEA**:

1. **Import** the project as a Maven project (`File → Open → select pom.xml`)
2. **Enable Lombok**:
   - Install the **Lombok** plugin (`Settings → Plugins → Marketplace → search "Lombok"`)
   - Enable annotation processing (`Settings → Build → Compiler → Annotation Processors → ✅ Enable`)
3. **Install Cucumber plugin** for `.feature` file syntax highlighting and step navigation

If using **VS Code**:

1. Install the **Extension Pack for Java**
2. Install the **Cucumber (Gherkin) Full Support** extension
3. Install the **Lombok Annotations Support** extension

---

## Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Tests in Headless Mode (No Browser UI)

```bash
mvn clean test -Dheadless=true
```

> Headless mode runs Chrome without a visible browser window — ideal for CI/CD pipelines.

### Run a Specific Scenario by Tag

```bash
# Run only the homepage link verification scenario
mvn clean test -Dcucumber.filter.tags="@TEST_TI_0001"

# Run only the Basic Auth scenario
mvn clean test -Dcucumber.filter.tags="@TEST_TI_0002"

# Run only the Sortable Data Tables scenario
mvn clean test -Dcucumber.filter.tags="@TEST_TI_0003"
```

### Run Multiple Tags

```bash
# Run scenarios tagged with EITHER tag (OR logic)
mvn clean test -Dcucumber.filter.tags="@TEST_TI_0001 or @TEST_TI_0002"

# Run scenarios tagged with BOTH tags (AND logic)
mvn clean test -Dcucumber.filter.tags="@TEST_TI_0001 and @TEST_TI_0002"

# Exclude a specific tag
mvn clean test -Dcucumber.filter.tags="not @TEST_TI_0003"
```

### Run from IDE

- **IntelliJ**: Right-click on `RunCucumberTest.java` → `Run`
- **IntelliJ**: Right-click on a `.feature` file → `Run`
- **VS Code**: Use the Testing panel or run `mvn test` from the integrated terminal

---

## Test Reports

After test execution, reports are generated in the following locations:

| Report Type        | Location                                     | Description                        |
|--------------------|----------------------------------------------|------------------------------------|
| **Cucumber HTML**  | `target/cucumber-report/cucumber.html`       | Built-in Cucumber HTML report      |
| **Surefire**       | `target/surefire-reports/`                   | Maven Surefire XML/TXT reports     |
| **Console Logs**   | Terminal output                               | Real-time `pretty` formatted output |
| **File Logs**      | `target/logs/test-automation.log`            | Persistent log file with timestamps |

### View the Cucumber HTML Report

```bash
# Open in default browser (Linux)
xdg-open target/cucumber-report/cucumber.html

# Open in default browser (macOS)
open target/cucumber-report/cucumber.html
```

> **Screenshots** are automatically captured after **every step** and attached to the Cucumber report. Failure screenshots are also captured separately for debugging.

---

## Test Scenarios

The framework covers 3 scenarios against [the-internet.herokuapp.com](https://the-internet.herokuapp.com):

### `@TEST_TI_0001` — Homepage Link Verification
Verifies that the homepage displays a complete list of expected example links (44 links).

### `@TEST_TI_0002` — Basic Auth Access
Navigates to the Basic Auth page, supplies valid credentials, and verifies the success message.

### `@TEST_TI_0003` — Sortable Data Tables Validation
Navigates to the Sortable Data Tables page and verifies that Example 1 displays 4 rows with the expected email addresses and website URLs.

---

## Framework Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                     theInternet.feature                          │
│              (Gherkin scenarios: Given/When/Then)                │
└──────────────────────┬───────────────────────────────────────────┘
                       │ matched by annotations
┌──────────────────────▼───────────────────────────────────────────┐
│              Step Definitions (glue/)                             │
│   HomeSteps · BasicAuthSteps · SortableDataTablesSteps           │
│                 ↑ extends Context                                │
└──────────────────────┬───────────────────────────────────────────┘
                       │ getDriver(), stash(), getStashed()
┌──────────────────────▼───────────────────────────────────────────┐
│              Context (Facade)  ←──── Hooks (@Before/@After)      │
│                 ↑ delegates to Manager                           │
└──────────────────────┬───────────────────────────────────────────┘
                       │
┌──────────────────────▼───────────────────────────────────────────┐
│              Manager (WebDriver + Test Data Stash)               │
│       PicoContainer creates one per scenario (isolation)         │
└──────────────────────┬───────────────────────────────────────────┘
                       │ WebDriver instance
┌──────────────────────▼───────────────────────────────────────────┐
│              Page Objects (pages/)                                │
│   BasePage (abstract) → HomePage · BasicAuthPage · ...           │
│       Encapsulates locators & page-specific actions              │
└──────────────────────┬───────────────────────────────────────────┘
                       │ drives
┌──────────────────────▼───────────────────────────────────────────┐
│              Chrome Browser (via Selenium WebDriver)             │
└──────────────────────────────────────────────────────────────────┘
```

### Key Design Patterns

| Pattern                  | Where Used                      | Purpose                                          |
|--------------------------|---------------------------------|--------------------------------------------------|
| **Page Object Model**    | `pages/` package                | Encapsulates UI locators & actions per page       |
| **Facade**               | `Context.java`                  | Simplifies access to shared resources             |
| **Dependency Injection** | PicoContainer + constructors    | Shares state across step classes per scenario     |
| **Template Method**      | `BasePage` abstract class       | Common actions (click, type, wait) reused by all pages |
| **Hooks**                | `Hooks.java`                    | Setup/teardown logic (driver init, screenshots)   |

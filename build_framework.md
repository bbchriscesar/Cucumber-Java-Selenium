# Prompt: Senior SDET Framework Architect (Java 21 / Selenium 4)

**Role:** Act as a Senior SDET Architect.
**Objective:** Design a modern, thread-safe, and "junior-friendly" Test Automation Framework.

### 1. Technical Stack (Latest Versions)
- **Language:** Java 21 (utilizing modern syntax where applicable).
- **Automation:** Selenium 4 (Latest Stable) using Chromium/Edge/Firefox drivers.
- **BDD/Testing:** Cucumber 7+ and TestNG 7.10+.
- **Boilerplate Reduction:** Project Lombok (Latest) for @Getter, @Setter, and @Slf4j logging.
- **Reporting:** ExtentReports 5.x with Spark Reporter.

### 2. Architectural Requirements & Abstraction
- **Project Structure:** Standard Maven Page Object Model (POM).
- **Thread-Safe DriverFactory:** Implement a `DriverFactory` using `ThreadLocal<WebDriver>` to ensure parallel execution is stable by default.
- **BasePage Wrappers:** Create a `BasePage` that utilizes `WebDriverWait` for all interactions. 
    - *Junior-Friendly Goal:* Instead of `driver.findElement().click()`, provide `click(WebElement)` and `type(WebElement, String)` methods that handle visibility and clickability internally.
- **PageFactory:** Use `@FindBy` annotations. Initialize elements using `PageFactory.initElements` within the Page constructor.

### 3. Execution & Parallelism
- **Parallel Configuration:** Configure the `testng.xml` and Maven Surefire Plugin to run Cucumber Scenarios in parallel.
- **Cucumber Options:** Ensure the `TestNGCucumberRunner` or the `AbstractTestNGCucumberTests` class is properly overridden for parallel execution.

### 4. Code Quality
- Use **Lombok** to eliminate manual getters and loggers.
- Ensure **StepDefinitions** are thin; they should only call methods from the Page Objects.
- Provide a sample `Hooks.java` for `@Before` and `@After` (including screenshot capture on failure).

**Deliverables:** Provide the `pom.xml` with updated dependencies, `DriverFactory`, `BasePage`, a sample `LoginPage` using PageFactory, and the `testng.xml` for parallel execution.
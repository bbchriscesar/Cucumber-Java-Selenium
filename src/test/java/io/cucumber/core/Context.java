package io.cucumber.core;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;

/*
 * INTERVIEW Q: What is the purpose of this Context class?
 * A: It acts as a FACADE that provides a simplified interface for step definition classes
 *    to access shared test resources (WebDriver, test data stash). Instead of step definitions
 *    interacting directly with the Manager class, they go through Context — which hides
 *    internal complexity and exposes only what steps actually need.
 *
 * INTERVIEW Q: How does PicoContainer inject this class into step definitions?
 * A: PicoContainer uses CONSTRUCTOR INJECTION. When a step definition class declares
 *    Context as a constructor parameter, PicoContainer:
 *    1. Sees that Context needs a Manager (via Context's own constructor)
 *    2. Creates a Manager instance first (since Manager has a no-arg constructor)
 *    3. Injects that Manager into a new Context instance
 *    4. Injects the Context into the step definition class
 *    This is called "transitive dependency resolution" — the DI container builds the
 *    entire object graph automatically.
 *
 * INTERVIEW Q: Why is Context scoped per scenario?
 * A: PicoContainer creates a NEW DI container for each Cucumber scenario.
 *    This means each scenario gets its own Context → Manager → WebDriver chain,
 *    ensuring complete TEST ISOLATION. No state leaks between scenarios.
 *
 * INTERVIEW Q: Why use a separate Context class instead of injecting Manager directly?
 * A: This follows the FACADE PATTERN and ENCAPSULATION principle:
 *    - Step definitions only see high-level operations (getDriver, stash, getStashed)
 *    - The internal structure (Manager, how stash works) can change without affecting steps
 *    - It limits what step definitions can do — they can't call manager.setDriver() directly
 */

// INTERVIEW Q: What does @Slf4j do?
// A: It's a Lombok annotation that generates a static SLF4J logger field at compile time:
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Context.class);

@Slf4j
public class Context {

    // INTERVIEW Q: Why is 'manager' declared as 'protected' instead of 'private'?
    // A: The 'protected' access modifier allows SUBCLASSES (in the same or different packages)
    //    to access this field directly. This is useful if a specialized context (e.g.,
    //    ApiContext or MobileContext) extends this class and needs direct access to Manager.
    //    Access modifiers in Java (narrowest → widest): private → default → protected → public.
    protected Manager manager;

    // INTERVIEW Q: Why is there no no-arg constructor?
    // A: This is intentional — it FORCES dependency injection. The Manager dependency MUST be
    //    provided via the constructor. This is called "constructor injection" and is considered
    //    the best DI practice because:
    //    1. Dependencies are explicit — you can see what a class needs from its constructor
    //    2. Objects are always in a valid state — no "half-initialized" objects
    //    3. Dependencies can be declared final (immutable after construction)
    //    PicoContainer detects this constructor and automatically provides a Manager instance.
    public Context(Manager manager) {
        this.manager = manager;
    }

    // INTERVIEW Q: Why return WebDriver interface instead of a concrete class like ChromeDriver?
    // A: This follows the "program to an interface, not an implementation" principle (Dependency
    //    Inversion Principle — the 'D' in SOLID). By returning the WebDriver interface:
    //    - Step definitions work with ANY browser (Chrome, Firefox, Edge) without code changes
    //    - The concrete driver is determined at RUNTIME by the Manager/DriverFactory
    //    - This enables easy cross-browser testing via configuration
    public WebDriver getDriver() {
        return manager.getDriver();
    }

    public HashMap<String, Object> getTestStash() {
        return manager.getStash();
    }

    // INTERVIEW Q: What does <T> mean in this method signature?
    // A: <T> is a GENERIC TYPE PARAMETER. It makes this method type-safe for any value type.
    //    Instead of accepting only Object, it captures the actual type at the call site.
    //    Example: stash("username", "admin") → T is inferred as String
    //             stash("count", 42)         → T is inferred as Integer
    //    This avoids the need for explicit casting when retrieving values later.
    public <T> void stash(String key, T value) {
        manager.stashValue(key, value);
    }

    // INTERVIEW Q: What does @SuppressWarnings("unchecked") mean and why is it here?
    // A: It tells the compiler to suppress "unchecked cast" warnings. The cast (T) on
    //    the Object value is UNSAFE at compile time because Java uses TYPE ERASURE —
    //    generic type information is removed at runtime, so the JVM can't verify the cast.
    //    We suppress the warning here because:
    //    - We control what goes into the stash via stash(), so we know the types are correct
    //    - The alternative (Class<T> token parameter) would add unnecessary complexity
    //    If the wrong type is retrieved, a ClassCastException is thrown at RUNTIME.
    //
    // INTERVIEW Q: What is Type Erasure?
    // A: Java generics are a COMPILE-TIME feature. The compiler checks types, then erases
    //    all generic information in the bytecode. At runtime, HashMap<String, Object> is
    //    just HashMap. This is why you can't do "new T()" or "instanceof T" in Java.
    @SuppressWarnings("unchecked")
    public <T> T getStashed(String key) {
        return (T) manager.getStash().get(key);
    }
}

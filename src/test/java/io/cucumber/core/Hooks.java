package io.cucumber.core;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Slf4j
public class Hooks extends Context {

    public Hooks(Manager manager) {
        super(manager);
    }

    @Before()
    public void before(Scenario scenario) {
        log.info("═══════════════════════════════════════════════════");
        log.info("Starting scenario: {}", scenario.getName());
        log.info("═══════════════════════════════════════════════════");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        if (headless) {
            options.addArguments("--headless=new");
        }

        manager.setDriver(new ChromeDriver(options));
        log.info("ChromeDriver initialized (headless={})", headless);
    }

    @BeforeStep
    public void beforeStep() {
        log.debug("Starting step...");
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        try {
            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Step Screenshot");
        } catch (Exception e) {
            log.warn("Failed to capture step screenshot: {}", e.getMessage());
        }
        log.debug("End of step.");
    }

    @After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("Scenario FAILED: {}", scenario.getName());
            try {
                byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
                log.info("Failure screenshot captured and attached.");
            } catch (Exception e) {
                log.warn("Failed to capture failure screenshot: {}", e.getMessage());
            }
        } else {
            log.info("Scenario PASSED: {}", scenario.getName());
        }

        getDriver().quit();
        log.info("ChromeDriver quit.");
        log.info("═══════════════════════════════════════════════════");
        log.info("Finished scenario: {} — Status: {}", scenario.getName(), scenario.getStatus());
        log.info("═══════════════════════════════════════════════════");
    }
}

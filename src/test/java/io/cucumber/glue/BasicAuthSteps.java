package io.cucumber.glue;

import io.cucumber.core.Context;
import io.cucumber.core.Manager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.pages.BasicAuthPage;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class BasicAuthSteps extends Context {

    public BasicAuthSteps(Manager manager) {
        super(manager);
    }

    @When("the {string} example is opened")
    public void theExampleIsOpened(String linkText) {
        log.info("Opening example: {}", linkText);
        stash("selectedExample", linkText);
    }

    @And("valid credentials are supplied")
    public void validCredentialsAreSupplied() {
        String baseUrl = getStashed("baseUrl");
        BasicAuthPage basicAuthPage = new BasicAuthPage(getDriver());
        basicAuthPage.navigateWithCredentials(baseUrl, "admin", "admin");
        stash("basicAuthPage", basicAuthPage);
    }

    @Then("Congratulations should be displayed")
    public void congratulationsShouldBeDisplayed() {
        BasicAuthPage basicAuthPage = getStashed("basicAuthPage");
        assertTrue(basicAuthPage.isCongratulationsDisplayed(),
                "Expected congratulations message to be displayed on Basic Auth page");
        String message = basicAuthPage.getCongratulationsMessage();
        log.info("Basic Auth message: {}", message);
        assertTrue(message.contains("Congratulations"),
                "Expected message to contain 'Congratulations' but got: " + message);
    }
}

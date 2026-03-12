package io.cucumber.glue;

import io.cucumber.core.Context;
import io.cucumber.core.Manager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.pages.HomePage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class HomeSteps extends Context {

    public HomeSteps(Manager manager) {
        super(manager);
    }

    @Given("the homepage displays a list of example links")
    public void theHomepageDisplaysExampleLinks() {
        HomePage homePage = new HomePage(getDriver());
        List<String> linkTexts = homePage.getExampleLinkTexts();
        stash("actualLinkTexts", linkTexts);
        log.info("Homepage displays {} example links", linkTexts.size());
        assertFalse(linkTexts.isEmpty(), "Homepage should display example links");
    }

    @Then("the list of examples should contain the following links:")
    public void theListShouldContainFollowingLinks(List<String> expectedLinks) {
        List<String> actualLinks = getStashed("actualLinkTexts");

        log.info("Verifying {} expected links against {} actual links",
                expectedLinks.size(), actualLinks.size());

        for (String expected : expectedLinks) {
            assertTrue(actualLinks.contains(expected.trim()),
                    "Expected link '" + expected + "' not found in the displayed list. " +
                            "Actual links: " + actualLinks);
        }

        assertEquals(expectedLinks.size(), actualLinks.size(),
                "Number of displayed links does not match expected count");
    }
}

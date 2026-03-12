package io.cucumber.glue;

import io.cucumber.core.Context;
import io.cucumber.core.Manager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.pages.SortableDataTablesPage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class SortableDataTablesSteps extends Context {

    public SortableDataTablesSteps(Manager manager) {
        super(manager);
    }

    @Given("the user navigates to the Sortable Data Tables page")
    public void theUserNavigatesToSortableDataTablesPage() {
        String baseUrl = getStashed("baseUrl");
        getDriver().get(baseUrl + "/tables");
        log.info("Navigated to Sortable Data Tables page");
    }

    @Then("Example 1 table should display {int} results")
    public void example1TableShouldDisplayResults(int expectedCount) {
        SortableDataTablesPage tablesPage = new SortableDataTablesPage(getDriver());
        int actualCount = tablesPage.getTable1RowCount();
        assertEquals(expectedCount, actualCount,
                "Expected " + expectedCount + " rows in Example 1 table but found " + actualCount);
    }

    @And("Example 1 table should contain the following emails and websites:")
    public void example1TableShouldContainFollowingData(io.cucumber.datatable.DataTable dataTable) {
        SortableDataTablesPage tablesPage = new SortableDataTablesPage(getDriver());

        List<Map<String, String>> expectedRows = dataTable.asMaps(String.class, String.class);
        List<String> actualEmails = tablesPage.getTable1Emails();
        List<String> actualWebsites = tablesPage.getTable1Websites();

        log.info("Verifying table data — Emails: {}, Websites: {}", actualEmails, actualWebsites);

        for (Map<String, String> expectedRow : expectedRows) {
            String expectedEmail = expectedRow.get("Email");
            String expectedWebsite = expectedRow.get("Web Site");

            assertTrue(actualEmails.contains(expectedEmail),
                    "Expected email '" + expectedEmail + "' not found in table. Actual: " + actualEmails);
            assertTrue(actualWebsites.contains(expectedWebsite),
                    "Expected website '" + expectedWebsite + "' not found in table. Actual: " + actualWebsites);
        }
    }
}

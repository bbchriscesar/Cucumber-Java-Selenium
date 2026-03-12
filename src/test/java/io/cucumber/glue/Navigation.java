package io.cucumber.glue;

import io.cucumber.core.Context;
import io.cucumber.core.Manager;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Navigation extends Context {

    public Navigation(Manager manager) {
        super(manager);
    }

    @Given("^the page under test is '(.+)'$")
    public void navToPage(String url) {
        log.info("Navigating to: {}", url);
        getDriver().get(url);
        stash("baseUrl", url);
    }
}

package io.cucumber.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Slf4j
public class BasicAuthPage extends BasePage {

    @FindBy(css = "#content .example p")
    private WebElement congratulationsMessage;

    public BasicAuthPage(WebDriver driver) {
        super(driver);
    }

    public String getCongratulationsMessage() {
        return getText(congratulationsMessage);
    }

    public boolean isCongratulationsDisplayed() {
        return isDisplayed(congratulationsMessage);
    }

    /**
     * Navigates to the Basic Auth page with embedded credentials in the URL.
     * Format: https://username:password@host/basic_auth
     */
    public void navigateWithCredentials(String baseUrl, String username, String password) {
        String authUrl = baseUrl.replaceFirst("://", "://" + username + ":" + password + "@") + "/basic_auth";
        log.info("Navigating to Basic Auth with embedded credentials: {}", authUrl);
        navigateTo(authUrl);
    }
}

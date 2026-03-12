package io.cucumber.pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class HomePage extends BasePage {

    @FindBy(css = "h1.heading")
    private WebElement heading;

    @FindBy(css = "#content ul li a")
    private List<WebElement> exampleLinks;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public String getHeadingText() {
        return getText(heading);
    }

    public List<String> getExampleLinkTexts() {
        List<String> linkTexts = exampleLinks.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.toList());
        log.info("Found {} example links on homepage", linkTexts.size());
        return linkTexts;
    }

    public void clickExampleLink(String linkText) {
        log.info("Clicking example link: {}", linkText);
        WebElement link = exampleLinks.stream()
                .filter(el -> el.getText().trim().equalsIgnoreCase(linkText))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Link with text '" + linkText + "' not found on homepage"));
        click(link);
    }

    public void clickExampleLinkContaining(String partialText) {
        log.info("Clicking example link containing: {}", partialText);
        WebElement link = exampleLinks.stream()
                .filter(el -> el.getText().trim().toLowerCase().contains(partialText.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Link containing '" + partialText + "' not found on homepage"));
        click(link);
    }
}

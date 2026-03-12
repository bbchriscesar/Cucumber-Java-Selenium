package io.cucumber.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;

@Slf4j
public class Manager {

    @Getter
    private WebDriver driver;

    final HashMap<String, Object> stash;

    public Manager() {
        this.stash = new HashMap<>();
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public <T> void stashValue(String key, T value) {
        log.debug("Stashing: {} = {}", key, value);
        stash.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T getStashedValue(String key) {
        return (T) stash.get(key);
    }

    public HashMap<String, Object> getStash() {
        return stash;
    }
}

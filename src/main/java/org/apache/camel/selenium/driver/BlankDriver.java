package org.apache.camel.selenium.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.Logs;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by marc.boulanger on 31/12/15.
 */
public class BlankDriver implements WebDriver {

    @Override
    public void get(String s) {

    }

    @Override
    public String getCurrentUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return new BlankWebElement();
    }

    @Override
    public String getPageSource() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void quit() {

    }

    @Override
    public Set<String> getWindowHandles() {
        return null;
    }

    @Override
    public String getWindowHandle() {
        return null;
    }

    @Override
    public TargetLocator switchTo() {
        return null;
    }

    @Override
    public Navigation navigate() {
        return null;
    }

    @Override
    public Options manage() {
        return new BlankOptions();
    }

    public class BlankOptions implements WebDriver.Options {

        @Override
        public void addCookie(Cookie cookie) {

        }

        @Override
        public void deleteCookieNamed(String s) {

        }

        @Override
        public void deleteCookie(Cookie cookie) {

        }

        @Override
        public void deleteAllCookies() {

        }

        @Override
        public Set<Cookie> getCookies() {
            return null;
        }

        @Override
        public Cookie getCookieNamed(String s) {
            return null;
        }

        @Override
        public WebDriver.Timeouts timeouts() {
            return new BlankTimeouts();
        }

        @Override
        public WebDriver.ImeHandler ime() {
            return null;
        }

        @Override
        public WebDriver.Window window() {
            return null;
        }

        @Override
        public Logs logs() {
            return null;
        }
    }

    public class BlankTimeouts implements Timeouts {

        @Override
        public Timeouts implicitlyWait(long l, TimeUnit timeUnit) {
            return this;
        }

        @Override
        public Timeouts setScriptTimeout(long l, TimeUnit timeUnit) {
            return null;
        }

        @Override
        public Timeouts pageLoadTimeout(long l, TimeUnit timeUnit) {
            return null;
        }
    }
}

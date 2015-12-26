package org.apache.camel.selenium.exceptions;

import org.apache.camel.selenium.beans.SeleniumAction;
import org.apache.camel.selenium.beans.SeleniumTest;

/**
 * Created by marc.boulanger on 26/12/15.
 */
public class SeleniumTestFailedException extends RuntimeException {

    SeleniumTest failedTest;
    SeleniumAction failedAction;

    public SeleniumTestFailedException(SeleniumTest seleniumTest, SeleniumAction seleniumAction) {
        this(null, seleniumTest, seleniumAction);
    }

    public SeleniumTestFailedException(Exception e, SeleniumTest seleniumTest, SeleniumAction seleniumAction) {
        super(e);
        this.failedTest = seleniumTest;
        this.failedAction = seleniumAction;
    }

    @Override
    public String getMessage() {
        return new StringBuilder("Error in Action : ").append(failedAction.getAction()).append("/").append(failedAction.getTarget()).append(" in Test : ").append(failedTest.getUrl()).toString();
    }

    public SeleniumTest getFailedTest() {
        return failedTest;
    }

    public SeleniumAction getFailedAction() {
        return failedAction;
    }
}

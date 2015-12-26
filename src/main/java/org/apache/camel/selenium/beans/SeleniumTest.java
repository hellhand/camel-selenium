package org.apache.camel.selenium.beans;

import java.util.List;

/**
 * Created by marc.boulanger on 25/12/15.
 */
public class SeleniumTest {

    private String url;
    private List<SeleniumAction> seleniumActionList;

    public SeleniumTest(String url, List<SeleniumAction> seleniumActionList) {
        this.url = url;
        this.seleniumActionList = seleniumActionList;
    }

    public String getUrl() {
        return url;
    }

    public List<SeleniumAction> getSeleniumActionList() {
        return seleniumActionList;
    }

    public static class SeleniumTestBuilder {
        private String url;
        private List<SeleniumAction> seleniumActionList;

        public SeleniumTestBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public SeleniumTestBuilder setSeleniumActionList(List<SeleniumAction> seleniumActionList) {
            this.seleniumActionList = seleniumActionList;
            return this;
        }

        public SeleniumTest build() {
            return new SeleniumTest(url, seleniumActionList);
        }
    }
}

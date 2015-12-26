package org.apache.camel.selenium.beans;

/**
 * Created by marc.boulanger on 25/12/15.
 */
public class SeleniumAction {

    private String action;
    private String target;
    private String value;

    private SeleniumAction(String action, String target, String value) {
        this.action = action;
        this.target = target;
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public String getValue() {
        return value;
    }

    public static class SeleniumActionBuilder {

        private String action;
        private String target;
        private String value;

        public SeleniumActionBuilder() {
        }

        public SeleniumAction build() {
            return new SeleniumAction(action, target, value);
        }

        public SeleniumActionBuilder setAction(String action) {
            this.action = action;
            return this;
        }

        public SeleniumActionBuilder setTarget(String target) {
            this.target = target;
            return this;
        }

        public SeleniumActionBuilder setValue(String value) {
            this.value = value;
            return this;
        }
    }
}

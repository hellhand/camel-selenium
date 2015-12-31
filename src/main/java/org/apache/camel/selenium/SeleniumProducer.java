/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.selenium;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.selenium.beans.SeleniumAction;
import org.apache.camel.selenium.beans.SeleniumTest;
import org.apache.camel.selenium.constants.SeleniumConstants;
import org.apache.camel.selenium.driver.BlankDriver;
import org.apache.camel.selenium.enums.Driver;
import org.apache.camel.selenium.exceptions.SeleniumTestFailedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class SeleniumProducer extends DefaultProducer {

    private static final transient Logger LOG = LoggerFactory.getLogger(SeleniumProducer.class);
    private SeleniumEndpoint endpoint;

    public SeleniumProducer(SeleniumEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        SeleniumTest seleniumTest = exchange.getIn().getBody(SeleniumTest.class);
        Driver driver = Optional.ofNullable(exchange.getIn().getHeader(SeleniumConstants.DRIVER)).isPresent() ? Driver.valueOf(exchange.getIn().getHeader(SeleniumConstants.DRIVER).toString()) : Driver.BLANK_DRIVER;
        executeActions(driver, seleniumTest);
        exchange.getOut().setBody(seleniumTest);
    }

    private void executeActions(final Driver driver, final SeleniumTest seleniumTest) {
        final WebDriver webDriver;
        switch (driver) {
            case CHROME_DRIVER:
                webDriver = new ChromeDriver();
                break;
            case SAFARI_DRIVER:
                webDriver = new SafariDriver();
                break;
            case FIREFOX_DRIVER:
                webDriver = new FirefoxDriver();
                break;
            case HTML_UNIT_DRIVER:
                webDriver = new HtmlUnitDriver();
                break;
            case BLANK_DRIVER:
            default:
                webDriver = new BlankDriver();
                break;
        }
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        seleniumTest.getSeleniumActionList().stream().forEach(new Consumer<SeleniumAction>() {
            @Override
            public void accept(SeleniumAction seleniumAction) {
                try {
                    Method method = SeleniumProducer.this.getClass().getDeclaredMethod(seleniumAction.getAction(), WebDriver.class, SeleniumTest.class, SeleniumAction.class);
                    method.setAccessible(true);
                    method.invoke(SeleniumProducer.this, webDriver, seleniumTest, seleniumAction);
                }
                catch (Exception e) {
                    webDriver.close();
                    throw new SeleniumTestFailedException(e, seleniumTest, seleniumAction);
                }
            }
        });
        webDriver.close();
        webDriver.quit();
    }

    @SuppressWarnings("unused")
    private void open(final WebDriver webDriver, final SeleniumTest seleniumTest, final SeleniumAction seleniumAction) {
        LOG.debug("open : Driver : {} - URL : {} - Action {}/{}/{}", Arrays.asList(webDriver, seleniumTest.getUrl(), seleniumAction.getAction(), seleniumAction.getTarget(), seleniumAction.getValue()).toArray());
        webDriver.get(seleniumTest.getUrl() + seleniumAction.getTarget());
    }

    @SuppressWarnings("unused")
    private void type(final WebDriver webDriver, final SeleniumTest seleniumTest, final SeleniumAction seleniumAction) {
        LOG.debug("type : Driver : {} - URL : {} - Action {}/{}/{}", Arrays.asList(webDriver, seleniumTest.getUrl(), seleniumAction.getAction(), seleniumAction.getTarget(), seleniumAction.getValue()).toArray());
        webDriver.findElement(getBy(seleniumAction.getTarget())).sendKeys(seleniumAction.getValue());
    }

    @SuppressWarnings("unused")
    private void click(final WebDriver webDriver, final SeleniumTest seleniumTest, final SeleniumAction seleniumAction) {
        LOG.debug("click : Driver : {} - URL : {} - Action {}/{}/{}", Arrays.asList(webDriver, seleniumTest.getUrl(), seleniumAction.getAction(), seleniumAction.getTarget(), seleniumAction.getValue()).toArray());
        webDriver.findElement(getBy(seleniumAction.getTarget())).click();
    }

    @SuppressWarnings("unused")
    private void clickAndWait(final WebDriver webDriver, final SeleniumTest seleniumTest, final SeleniumAction seleniumAction) throws InterruptedException {
        LOG.debug("clickAndWait : Driver : {} - URL : {} - Action {}/{}/{}", Arrays.asList(webDriver, seleniumTest.getUrl(), seleniumAction.getAction(), seleniumAction.getTarget(), seleniumAction.getValue()).toArray());
        webDriver.findElement(getBy(seleniumAction.getTarget())).click();
        Thread.sleep(2000);
    }

    @SuppressWarnings("unused")
    private void waitForElementPresent(final WebDriver webDriver, final SeleniumTest seleniumTest, final SeleniumAction seleniumAction) throws TimeoutException, InterruptedException {
        LOG.debug("waitForElementPresent : Driver : {} - URL : {} - Action {}/{}/{}", Arrays.asList(webDriver, seleniumTest.getUrl(), seleniumAction.getAction(), seleniumAction.getTarget(), seleniumAction.getValue()).toArray());
        for (int second = 0; ; second++) {
            if (second >= 60) throw new TimeoutException();
            try {
                webDriver.findElement(getBy(seleniumAction.getTarget()));
                return;
            }
            catch (Exception e) {
            }
            Thread.sleep(1000);
        }
    }

    @SuppressWarnings("unused")
    private void assertText(final WebDriver webDriver, final SeleniumTest seleniumTest, final SeleniumAction seleniumAction) throws InterruptedException {
        LOG.debug("assertText : Driver : {} - URL : {} - Action {}/{}/{}", Arrays.asList(webDriver, seleniumTest.getUrl(), seleniumAction.getAction(), seleniumAction.getTarget(), seleniumAction.getValue()).toArray());
        webDriver.findElement(getBy(seleniumAction.getTarget()));
    }

    private By getBy(final String target) {
        final List<String> clazz = new ArrayList<String>();
        Optional.ofNullable(target).ifPresent(new Consumer<String>() {
            @Override
            public void accept(String s) {
                Collections.addAll(clazz, target.split("="));
            }
        });
//        try {
//            Class by = Class.forName("org.openqa.selenium.By.By" + Character.toUpperCase(clazz.get(0).charAt(0)) + clazz.get(0).substring(1));
//            return (By) by.getConstructor(String.class).newInstance(clazz.get(1));
//        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//        catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;

        if ("id".equals(clazz.get(0))) {
            return By.id(clazz.get(1));
        }
        if ("class".equals(clazz.get(0))) {
            return By.className(clazz.get(1));
        }
        if ("css".equals(clazz.get(0))) {
            return By.cssSelector(clazz.get(1));
        }
        if ("link".equals(clazz.get(0))) {
            return By.linkText(clazz.get(1));
        }
        return null;
    }
}

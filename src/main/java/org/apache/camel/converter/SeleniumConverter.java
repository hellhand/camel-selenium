package org.apache.camel.converter;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.selenium.beans.SeleniumAction;
import org.apache.camel.selenium.beans.SeleniumTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by marc.boulanger on 25/12/15.
 */
@Converter
public class SeleniumConverter {

    @Converter
    public SeleniumTest toSeleniumTest(Message message) {
        String html = message.getBody(String.class);
        Document document = Jsoup.parse(html);

        String url = document.select("link").first().attr("href");
        List<SeleniumAction> seleniumActionList = document.select("tbody").select("tr").stream().map(new Function<Element, SeleniumAction>() {
            @Override
            public SeleniumAction apply(Element element) {
                Elements step = element.select("td");
                return new SeleniumAction.SeleniumActionBuilder().setAction(step.get(0).text()).setTarget(step.get(1).text()).setValue(step.get(2).text()).build();
            }
        }).collect(Collectors.<SeleniumAction>toList());

        return new SeleniumTest.SeleniumTestBuilder().setUrl(url).setSeleniumActionList(seleniumActionList).build();
    }
}

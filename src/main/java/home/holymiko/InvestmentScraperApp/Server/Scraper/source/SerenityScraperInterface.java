package home.holymiko.InvestmentScraperApp.Server.Scraper.source;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public interface SerenityScraperInterface {

    default String getRatingOrResult(HtmlPage page, int i) {
        return ((HtmlElement) page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div["+i+"]/div[2]/div")).asText();
    }

    default String scrapHeader(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath("/html/body/div[2]/div/section/h1")).asText();
    }

    default String scrapRatingScore(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span")).asText();
    }

    default String scrapCurrency(HtmlPage page) {
        return ((DomText) page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div[9]/div/div/text()")).asText();
    }

}

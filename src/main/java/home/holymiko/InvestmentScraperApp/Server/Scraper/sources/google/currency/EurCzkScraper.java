package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.google.currency;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import home.holymiko.InvestmentScraperApp.Server.Entity.CurrencyRatio;
import home.holymiko.InvestmentScraperApp.Server.Enum.Currency;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Scraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EurCzkScraper extends Scraper {
    private static final String SEARCH_URL = "https://www.google.com/search?q=kurz+eur+czk";
    private static final String X_PATH = "//*[@id=\"knowledge-currency__updatable-data-column\"]/div[1]/div[2]";

    @Autowired
    public EurCzkScraper() {
    }

    public CurrencyRatio scrap() {
        System.out.print("eurCzk ");
        loadPage(SEARCH_URL);

        CurrencyRatio currencyRatio = null;
        // TODO Napojit XPath
//        String ratio = ((HtmlElement) page.getFirstByXPath(X_PATH)).asText();
//        System.out.println(ratio);

        try {
            currencyRatio = new CurrencyRatio(
                    Currency.EUR,
                    Currency.CZK,
                    25.4
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
//        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(X_PATH);
        return currencyRatio;
    }
}

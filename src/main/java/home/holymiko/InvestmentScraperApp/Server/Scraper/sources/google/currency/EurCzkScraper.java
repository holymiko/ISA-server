package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.google.currency;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.InvestmentScraperApp.Server.Entity.CurrencyRatio;
import home.holymiko.InvestmentScraperApp.Server.Enum.Currency;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Scraper;
import org.springframework.stereotype.Component;


@Component
public class EurCzkScraper extends Scraper {
    private static final String SEARCH_URL = "https://www.google.com/search?q=kurz+eur+czk";
    private static final String X_PATH = "//*[@id=\"knowledge-currency__updatable-data-column\"]/div[1]";


    public CurrencyRatio scrap() {
        CurrencyRatio currencyRatio = null;

        loadPage(SEARCH_URL);
        try {
            currencyRatio = new CurrencyRatio(
                    Currency.EUR,
                    Currency.CZK,
                    Double.parseDouble((
                            (HtmlElement) page.getFirstByXPath(X_PATH)
                    ).asText())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
//        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(X_PATH);
        System.out.println("eurCzk " + currencyRatio.getRatio());
        return currencyRatio;
    }
}

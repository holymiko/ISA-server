package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;

import java.util.Arrays;
import java.util.List;

public class GoldSafeAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.GOLD_SAFE;
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.goldsafe.cz/zlato",
            "https://www.goldsafe.cz/stribro"
    );


    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"products-holder\"]/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = "./a";
    private static final String X_PATH_PRODUCT_NAME = "/html/body/div[3]/div/div[1]/h1";
    private static final String X_PATH_BUY_PRICE = "/html/body/div[3]/div/div[4]/div[1]/div[2]/span[1]";
    private static final String X_PATH_SELL_PRICE = "/html/body/div[3]/div/div[4]/div[1]/div[2]/div/span[2]";
    private static final String X_PATH_AVAILABILITY = "/html/body/div[3]/div/div[4]/table/tbody/tr[6]/td[2]";
    private static final String X_PATH_AVAILABILITY_2 = "/html/body/div[3]/div/div[4]/table/tbody/tr[7]/td[2]";

    public GoldSafeAdapter() {
        super("GoldSafeAdapter");
    }

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }

    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        return scrapAllLinksFromProductListUtil(PRODUCT_LIST_URL);
    }

    /////// PRICE

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) {
        return Convert.currencyToDouble(
                ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText()
                        .replace(".", "")
        );
    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        return Convert.currencyToDouble(
                ((HtmlElement) page.getFirstByXPath(X_PATH_SELL_PRICE)).asText()
                        .replace(".", "")
        );
    }

    /////// LINK
    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLinkFromAnchor(elementProduct, X_PATH_PRODUCT_LIST_ANCHOR, DEALER, "");
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        String availabilityMsg = ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
        try {
            Convert.availability(availabilityMsg);
        } catch (NullPointerException | IllegalArgumentException e) {
            availabilityMsg = ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY_2)).asText();
        }
        return availabilityMsg;
    }

}

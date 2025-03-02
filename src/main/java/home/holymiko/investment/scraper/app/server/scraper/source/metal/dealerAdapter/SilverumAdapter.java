package home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.core.exception.ScrapFailedException;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Extract;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.scraper.source.Client;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Convert;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SilverumAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.SILVERUM;
    private static final String BASE = "https://www.silverum.cz/";
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.silverum.cz/svetove-investicni-mince.html",
            "https://www.silverum.cz/mince-cnb.html",
            "https://www.silverum.cz/investicni-slitky.html",
            "https://www.silverum.cz/investicni-cihly.html",
            // Silver
            "https://www.silverum.cz/svetove-mince-2023-24.html",
            "https://www.silverum.cz/svetove-mince-do-2022.html",
            "https://www.silverum.cz/lunarni-serie.html",
            "https://www.silverum.cz/ceske-mince-cnb-cs.html",
            "https://www.silverum.cz/investicni-slitky-a-medaile.html",
            "https://www.silverum.cz/investicni-cihly-cs.html"
    );

    private static final String X_PATH_PRODUCT_LIST = ".//div[@class='productItem']";
    private static final String X_PATH_PRODUCT_NAME = ".//h1[@class='title f600']";
    private static final String X_PATH_PRODUCT_LIST_NAME = ".//h3[@class='title f600']";

    private static final String X_PATH_BUY_PRICE = ".//strong[@class='pricePerItem']";
    private static final String X_PATH_BUY_PRICE_LIST = ".//div[@class='price f600']";

    private static final String X_PATH_BID_SPOT_GOLD = "/html/body/section[1]/div/div/div[1]/div/div/div[2]/div";
    private static final String X_PATH_BID_SPOT_SILVER  = "/html/body/section[1]/div/div/div[2]/div/div/div[2]/div";
    private static final String X_PATH_BID_SPOT_DOLLAR = "/html/body/section[1]/div/div/div[4]/div/div/div[2]/div";
    private static final String X_PATH_AVAILABILITY = "/html/body/main/div/section[1]/div/div[2]/div[2]/div/form/div[3]/div";
    private static final String X_PATH_AVAILABILITY_2 = "/html/body/main/div/section[1]/div/div[2]/div[2]/div/div[2]/div";

    public SilverumAdapter() {
        super("SilverumAdapter");
    }

    /////// LINK

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }

    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        return scrapAllLinksFromProductListUtil(PRODUCT_LIST_URL);
    }

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLinkFromAnchor(elementProduct, "./a", DEALER, BASE);
    }


    /////// PRODUCT

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }


    /////// PRICE

    @Override
    public double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) {
        String x = ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText();
        if(Pattern.compile("\\d+,\\d+ Kč bez DPH").matcher(x).find()) {
            x = x.split("\\d*[ ]?\\d+,\\d+ Kč bez DPH")[0];
        }
        return Convert.currencyToDouble(x);
    }

    /**
     * Computed according to https://www.silverum.cz/zpetny-vykup.html
     * TODO Call to Silverum and double-check the buyout computation is correct
     * @param page
     * @return
     */
    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        final String productName = scrapNameFromProductPage(page).toLowerCase();
        final Metal metal = Extract.metalExtract(productName);
        final double weight = Extract.weightExtract(productName) / Extract.TROY_OUNCE;
        final Double dollar;
        try {
            dollar = Convert.currencyToDouble(
                    ((HtmlElement) page.getFirstByXPath(X_PATH_BID_SPOT_DOLLAR)).asText()
            );
        } catch (NullPointerException e) {
            // TODO resolve NULL pointer exception or validate that this is correct behaviour
            return 0;
        }
        String metalDollarSpotTxt;
        double q = 1;

        if(metal == Metal.GOLD) {
            metalDollarSpotTxt = ((HtmlElement) page.getFirstByXPath(X_PATH_BID_SPOT_GOLD)).asText();
            if(weight > Extract.TROY_OUNCE) {
                q = 0.97;
            }
        } else if(metal == Metal.SILVER) {
            metalDollarSpotTxt = ((HtmlElement) page.getFirstByXPath(X_PATH_BID_SPOT_SILVER)).asText();
            // American Silver Eagle is +2,5%
            if(productName.contains("american") && productName.contains("eagle")) {
                q = 1.025;
            }
            if(weight > 100 * Extract.TROY_OUNCE) {
                q = 0.93;
            }
        } else {
            throw new ScrapFailedException("ERROR: Silverum unexpected product");
        }

        return Convert.currencyToDouble( metalDollarSpotTxt ) * dollar * weight * q;
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        try {
            return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
        } catch (Exception ignore) {
            return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY_2)).asText();
        }
    }

}

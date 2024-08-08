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

public class AurumBohemicaAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.AURUM_BOHEMICA;
    private static final String BASE_URL = "https://www.aurumbohemica.cz";
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-2/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-3/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-4/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-5/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-6/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-7/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-8/",
            "https://www.aurumbohemica.cz/investicni-zlate-slitky/strana-9/",
            "https://www.aurumbohemica.cz/investicni-mince-1/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-1/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-2/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-3/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-4/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-5/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-6/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-7/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-8/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-9/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-10/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-11/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-12/",
            "https://www.aurumbohemica.cz/investicni-mince-1/strana-13/",
            "https://www.aurumbohemica.cz/investicni-stribrne-slitky/",
            "https://www.aurumbohemica.cz/investicni-stribrne-slitky/strana-2/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-2/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-3/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-4/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-5/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-6/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-7/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-8/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-9/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-10/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-11/",
            "https://www.aurumbohemica.cz/investicni-stribrne-mince/strana-12/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-2/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-3/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-4/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-5/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-6/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-7/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-8/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-9/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-10/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-11/",
            "https://www.aurumbohemica.cz/zlate-mince-cnb/strana-12/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-2/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-3/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-4/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-5/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-6/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-7/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-8/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-2/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-3/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-4/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-5/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-6/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-7/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-8/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-9/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-10/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-11/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-12/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-13/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-14/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-15/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-16/",
            "https://www.aurumbohemica.cz/zlate-historicke-mince/strana-17/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-2/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-3/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-4/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-5/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-6/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-7/",
            "https://www.aurumbohemica.cz/stribrne-mince-cnb/strana-8/",
            "https://www.aurumbohemica.cz/historicke-novorazby/",
            "https://www.aurumbohemica.cz/historicke-novorazby/strana-2/",
            "https://www.aurumbohemica.cz/historicke-novorazby/strana-3/"
    );


    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"products\"]/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = "./div/a";
    private static final String X_PATH_PRODUCT_NAME = "//*[@class=\"p-detail-inner-header\"]/h1";
    private static final String X_PATH_BUY_PRICE = "//*[@class=\"price-final-holder\"]";
    private static final String X_PATH_SELL_PRICE = "//*[@class=\"col-xs-12 col-lg-6 p-info-wrapper\"]/div[4]";     // TODO This doesnt work
    private static final String X_PATH_AVAILABILITY = "//*[@class=\"availability-label\"]";


    public AurumBohemicaAdapter() {
        super("AurumBohemicaAdapter");
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
        );
    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
//        TODO
//        ((HtmlDivision) page.getFirstByXPath("//*[@class=\"col-xs-12 col-lg-6 p-info-wrapper\"]")).getChildNodes().forEach(x ->
//                LOGGER.info("Some5 {}", x)
//        );

        return Convert.currencyToDouble(
                ((HtmlElement) page.getFirstByXPath(X_PATH_SELL_PRICE)).asText()
        );
    }

    /////// LINK
    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLinkFromAnchor(elementProduct, X_PATH_PRODUCT_LIST_ANCHOR, DEALER, BASE_URL);
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
    }

}

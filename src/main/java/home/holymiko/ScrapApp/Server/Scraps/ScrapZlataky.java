package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.Link;
import home.holymiko.ScrapApp.Server.Entity.Price;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class ScrapZlataky extends ScrapMetal {

    private static final String BASE_URL = "https://zlataky.cz";
    private static final String searchUrlGoldCoin = "https://zlataky.cz/investicni-zlate-mince?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";
    private static final String searchUrlGoldBar = "https://zlataky.cz/investicni-zlate-slitky?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";

    private static final String searchUrlSilverCoin = "https://zlataky.cz/investicni-stribrne-mince?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";
    private static final String searchUrlSilverBar = "https://zlataky.cz/investicni-stribrne-slitky?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";

    private static final String searchUrlPlatinum = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=1&filter_weight=on&filter_id=on&sort=3a&filter_in_stock=1";
    private static final String searchUrlPalladium = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=2&filter_weight=on&filter_id=on&sort=3a&filter_in_stock=1";
    private static final String searchUrlRhodium = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=3&filter_weight=on&filter_id=on&sort=1a&filter_in_stock=1";

    @Autowired
    public ScrapZlataky(LinkService linkService,
                        PriceService priceService,
                        ProductService productService,
                        PortfolioService portfolioService,
                        InvestmentService investmentService) {
        super(  Dealer.ZLATAKY,
                linkService, priceService, portfolioService, productService, investmentService,
                "//*[@id=\"productListing\"]/div");
    }

    /////// PRODUCT

    @Override
    protected void byLink(Link link) {
        double weight;
        Product product;

        loadPage(link.getLink());
        HtmlElement name = page.getFirstByXPath(".//span[@class='base']");
        weight = weightExtractor(name.asText());
        Form form = formExtractor(name.asText());
        Producer producer = producerExtractor(name.asText());

        if (name.asText().contains("Zlat"))
            product = new Product( producer, form, Metal.GOLD, name.asText(), weight, link, null, new ArrayList<>() );
        else if (name.asText().contains("Stříbr"))
            product = new Product( producer, form, Metal.SILVER, name.asText(), weight, link, null, new ArrayList<>());
        else if (name.asText().contains("Platin"))
            product = new Product( producer, form, Metal.PLATINUM, name.asText(), weight, link, null, new ArrayList<>());
        else if (name.asText().contains("Pallad"))
            product = new Product( producer, form, Metal.PALLADIUM, name.asText(), weight, link, null, new ArrayList<>());
        else
            product = new Product( producer, form, Metal.UNKNOWN, name.asText(), weight, link, null, new ArrayList<>());

        addPriceToProduct(product, scrapPrice(product));
        System.out.println("Product saved");
    }

    /////// PRICE

    @Override
    protected Price scrapPrice(Product product) {
        loadPage(product.getLink().getLink());

        double weight = product.getGrams();
        HtmlElement htmlBuyPrice = page.getFirstByXPath(".//span[@class='price']");
        HtmlElement htmlRedemptionPrice = page.getFirstByXPath(".//div[@class='vykupni-cena']");

        String buyPrice = formatPrice(htmlBuyPrice.asText());
        String redPrice = htmlRedemptionPrice.asText();
        try {
            redPrice = redPrice.split(":")[1];          // Aktuální výkupní cena (bez DPH): xxxx,xx Kč
        } catch (Exception e) {
            e.printStackTrace();
        }
        redPrice = formatPrice(redPrice);
        if (Double.parseDouble(redPrice) <= 0.0)
            System.out.println("WARNING - Vykupni cena = 0");
        Price newPrice = new Price(LocalDateTime.now(), Double.parseDouble(buyPrice), Double.parseDouble(redPrice), weight);
        addPriceToProduct(product, newPrice);
        System.out.println("> New price saved");
        return newPrice;
    }


    /////// LINK

    @Override
    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div/h3/a");
        if(itemAnchor != null) {
            Link link = new Link(Dealer.ZLATAKY, BASE_URL + itemAnchor.getHrefAttribute());
            linkFilterAction(link);
            return;
        }
        System.out.println("Error: "+htmlItem.asText());
    }




    @Override
    public void sAllLinks() {
        scrapLinks(searchUrlGoldCoin);
        scrapLinks(searchUrlGoldBar);

        scrapLinks(searchUrlSilverCoin);
        scrapLinks(searchUrlSilverBar);

        scrapLinks(searchUrlPlatinum);
        scrapLinks(searchUrlPalladium);
        scrapLinks(searchUrlRhodium);
    }
    @Override
    public void sGoldLinks() {
        scrapLinks(searchUrlGoldCoin);
        scrapLinks(searchUrlGoldBar);
    }
    @Override
    public void sSilverLinks() {
        scrapLinks(searchUrlSilverCoin);
        scrapLinks(searchUrlSilverBar);
    }
    @Override
    public void sPlatinumLinks() {
        scrapLinks(searchUrlPlatinum);
    }
    @Override
    public void sPalladiumLinks() {
        scrapLinks(searchUrlPalladium);
    }

}

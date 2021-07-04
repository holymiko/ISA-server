package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class ScrapBessergold extends ScrapMetal {
    private static final String searchUrlGold = "https://www.bessergold.cz/investicni-zlato.html?product_list_limit=all";
    private static final String searchUrlSilver = "https://www.bessergold.cz/investicni-stribro.html?product_list_limit=all";
    private static final String searchUrlPlatinum = "https://www.bessergold.cz/investicni-platina.html?product_list_limit=all";
    private static final String searchUrlPalladium = "https://www.bessergold.cz/investicni-palladium.html?product_list_limit=all";

    private static final String xPathProductList = "//li[@class='item product product-item']";

    @Autowired
    public ScrapBessergold(LinkService linkService,
                           PriceService priceService,
                           ProductService productService,
                           PortfolioService portfolioService,
                           InvestmentService investmentService) {
        super(
                Dealer.BESSERGOLD,
                linkService,
                priceService,
                portfolioService,
                productService,
                investmentService,
                searchUrlGold,
                searchUrlSilver,
                searchUrlPlatinum,
                searchUrlPalladium,
                xPathProductList
        );
    }

    /////// PRODUCT

    @Override
    protected void productByLink(Link link) {
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

        addPriceToProduct(product, priceByProduct(product));
        System.out.println("Product saved");
    }

    /////// PRICE

    /**
     * Scraps new price for already known product
     * @param product already saved in DB
     * @return new price
     */
    @Override
    protected Price priceByProduct(Product product) {
        loadPage(product.getLink().getLink());

        double weight = product.getGrams();
        HtmlElement htmlBuyPrice = page.getFirstByXPath(".//span[@class='price']");
        HtmlElement htmlRedemptionPrice = page.getFirstByXPath(".//div[@class='vykupni-cena']");

        Double buyPrice = formatPrice(htmlBuyPrice.asText());
        Double redemptionPrice = 0.0;
        try {
            String redemptionPriceText = htmlRedemptionPrice.asText().split(":")[1];          // Aktuální výkupní cena (bez DPH): xxxx,xx Kč
            redemptionPrice = formatPrice(redemptionPriceText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (redemptionPrice <= 0.0) {
            System.out.println("WARNING - Vykupni cena = 0");
        }
        Price newPrice = new Price(LocalDateTime.now(), buyPrice, redemptionPrice, weight);
        addPriceToProduct(product, newPrice);
        System.out.println("> New price saved");
        return newPrice;
    }


    /////// LINK

    @Override
    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//strong[@class='product name product-item-name']/a");
        Link link = new Link(Dealer.BESSERGOLD, itemAnchor.getHrefAttribute());
        linkFilterWrapper(link);
    }
}

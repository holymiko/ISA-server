package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductListInterface {

    default List<Link> scrapAllLinksFromProductLists() {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    /**
     * Finds list of elements, based on class variable xPathProductList
     * For each calls scrapLink abstract method.
     * @param page
     * @return
     */
    default List<Link> scrapLinksFromProductList(HtmlPage page) {
        // Scraps new link for each element
        return scrapProductList(page).stream()
                .map(
                        this::scrapLink
                ).collect(Collectors.toList());
    }

    default List<HtmlElement> scrapProductList(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default Link scrapLink(HtmlElement elementProduct) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default Link scrapLink(HtmlElement elementProduct, String xPathToLink, Dealer dealer, String baseUrl) {
        HtmlAnchor itemAnchor = elementProduct.getFirstByXPath(xPathToLink);
        if(itemAnchor == null) {
            System.out.println("Error: "+ elementProduct.asText());
            return null;
        }
        return new Link(dealer, baseUrl + itemAnchor.getHrefAttribute());
    }

}

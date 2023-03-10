package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO Consider refactoring to abstract class
public interface ProductListInterface {
    Logger LOGGER = LoggerFactory.getLogger(ProductListInterface.class);

    default HtmlPage getPage(final String link) throws ResourceNotFoundException {
        throw new NotImplementedException("Method haven't been implemented yet");
    }
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

    default List<Link> scrapAllLinksFromProductListUtil(List<String> urls) {
        List<Link> elements = new ArrayList<>();
        for (String url:urls) {
            try {
                elements.addAll(scrapLinksFromProductList(getPage(url)));
            } catch (ResourceNotFoundException e) {
                e.printStackTrace();
            }
        }
        return elements;
    }

    default List<HtmlElement> scrapProductList(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default Link scrapLink(HtmlElement elementProduct) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    /**
     * TODO Refactor name to scrapLinkAndName
     *  Add xPathToName parameter
     *  To adapters, add corresponding xPath constant 'X_PATH_PRODUCT_LIST_PRODUCT_NAME' matching product name
     *  Use Link constructor including 'name'
     *  Drop DB and check if it loads data
     */
    default Link scrapLink(HtmlElement elementProduct, String xPathToLink, Dealer dealer, String baseUrl) {
        HtmlAnchor itemAnchor = elementProduct.getFirstByXPath(xPathToLink);
        if(itemAnchor == null) {
            LOGGER.error(elementProduct.asText());
            return null;
        }
        return new Link(dealer, baseUrl + itemAnchor.getHrefAttribute());
    }

}

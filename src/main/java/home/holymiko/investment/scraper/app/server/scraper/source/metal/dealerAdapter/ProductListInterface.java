package home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
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
        throw new NotImplementedException("Method 'getPage' haven't been implemented yet");
    }
    default List<Link> scrapAllLinksFromProductLists() {
        throw new NotImplementedException("Method 'scrapAllLinksFromProductLists' haven't been implemented yet");
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
        throw new NotImplementedException("Method 'scrapProductList' haven't been implemented yet");
    }

    default Link scrapLink(HtmlElement elementProduct) {
        throw new NotImplementedException("Method 'scrapLink' haven't been implemented yet");
    }

    /**
     * TODO Refactor name to scrapLinkAndName
     *  Add xPathToName parameter
     *  To adapters, add corresponding xPath constant 'X_PATH_PRODUCT_LIST_PRODUCT_NAME' matching product name
     *  Use Link constructor including 'name'
     *  Drop DB and check if it loads data
     */
    default Link scrapLinkFromAnchor(HtmlElement elementProduct, String xPathToAnchor, Dealer dealer, String prefixUrl) {
        HtmlAnchor itemAnchor = elementProduct.getFirstByXPath(xPathToAnchor);
        if(itemAnchor == null) {
            LOGGER.error(elementProduct.asText());
            return null;
        }
        final String url = prefixUrl + itemAnchor.getHrefAttribute();
        LOGGER.info("FOUND: " + url);
        return new Link(dealer, url, "TODO");
    }

}

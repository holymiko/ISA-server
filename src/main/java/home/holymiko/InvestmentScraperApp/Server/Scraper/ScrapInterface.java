package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;

public interface ScrapInterface {

    /**
     * Sets class variable page.
     * @param link URL of the page
     * @return True if page was found and loaded successfully.
     */
    default HtmlPage loadPage(WebClient client, final String link) throws ResourceNotFoundException {
        // TODO Test cases: No connection, 404 page, etc.
        HtmlPage page;
        try {
            page = client.getPage(link);                // Product page
        } catch (Exception e)  {
            throw new ResourceNotFoundException("loadPage exception");
        }
        if(page.getWebResponse().getStatusCode() == 404) {
            throw new ResourceNotFoundException("loadPage exception - 404");
        }
        return page;
    }

    default TextPage loadTextPage(WebClient client, final String link) throws ResourceNotFoundException {
        try {
            return client.getPage(link);
        } catch (Exception e)  {
            throw new ResourceNotFoundException("Page not found - "+link);
        }
    }
}

package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;

public class Scraper {
    protected HtmlPage page;
    protected final WebClient client;
    protected static int printerCounter = 0;

    public Scraper() {
        client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    /**
     * Sets class variable page.
     * @param link URL of the page
     * @return True if page was found and loaded successfully.
     */
    protected boolean loadPage(final String link){
        // TODO instead of boolean, throw ResourceNotFoundException
        // TODO Test cases: No connection, 404 page, etc.
        page = null;
        try {
            page = client.getPage(link);                // Product page
        } catch (Exception e)  {
//            e.printStackTrace();
            return false;
        }
//        assert page != null;
        return page.getWebResponse().getStatusCode() != 404;
    }

    protected TextPage loadTextPage(final String link) {
        try {
            return client.getPage(link);
        } catch (Exception e)  {
            throw new ResourceNotFoundException("Page not found - "+link);
        }
    }

}

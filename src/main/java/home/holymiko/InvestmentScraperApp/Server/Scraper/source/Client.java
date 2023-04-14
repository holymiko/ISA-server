package home.holymiko.InvestmentScraperApp.Server.Scraper.source;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;

public class Client {

    // TODO Decide if it can be make static (includes deleting MetalAdapterInterface.getPage()
    //  or if its necessary to keep it for multithreading.
    //  Implement multithreading and measure performance for static/non static
    protected final WebClient client;

    public Client() {
        client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    /**
     * Sleep time is dynamic, according to time took by scrap procedure
     * @param ethical_delay Constant
     * @param startTime Time of scrap procedure start
     */
    public static void dynamicSleep(final long ethical_delay, final long startTime) {
        long delay = ethical_delay - (System.nanoTime()/1_000_000 - startTime/1_000_000);
        if(delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets class variable page.
     * @param link URL of the page
     * @return True if page was found and loaded successfully.
     */
    protected HtmlPage loadPage(final String link) throws ResourceNotFoundException {
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

    protected TextPage loadTextPage(final String link) throws ResourceNotFoundException {
        try {
            return client.getPage(link);
        } catch (Exception e)  {
            throw new ResourceNotFoundException("Page not found - "+link);
        }
    }

}

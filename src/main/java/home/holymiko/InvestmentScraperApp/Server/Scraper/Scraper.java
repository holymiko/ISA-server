package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;

public class Scraper implements ScrapInterface {
    protected HtmlPage page;
    protected final WebClient client;

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
    protected HtmlPage loadPage(final String link) throws ResourceNotFoundException {
        return loadPage(client, link);
    }

    protected TextPage loadTextPage(final String link) throws ResourceNotFoundException {
        return loadTextPage(client, link);
    }

}

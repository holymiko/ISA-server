package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Scrap {
    protected HtmlPage page;
    protected final WebClient client;
    protected static int printerCounter = 0;

    public Scrap() {
        client = new WebClient();
        setClient();
    }

    private void setClient() {
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    protected void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sleep time is dynamic, according to time took by scrap procedure
     * @param ethical_delay Constant
     * @param start Time of scrap procedure start
     * @param size of list being scraped
     */
    protected void dynamicSleepAndStatusPrint(final long ethical_delay, final long start, final int size){
        printerCounter++;
        if ((printerCounter % 10) == 0) {
            System.out.println(printerCounter + "/" + size);
        }

        // Sleep time is dynamic, according to time took by scrap procedure
        long delay = ethical_delay - (System.nanoTime()-start);

        if(delay > 0){
            sleep(delay);
        }
    }

    /**
     * Sets class variable page.
     * @param link URL of the page
     * @return True if page was found and loaded successfully.
     */
    protected boolean loadPage(String link){
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

}

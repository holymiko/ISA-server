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

    private void sleep(final long delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void dynamicSleepAndStatusPrint(final long ethical_delay, final long startTime, final int interval, final int size) {
        statusPrint(interval, size);
        dynamicSleep(ethical_delay, startTime);
    }

    /**
     * Sleep time is dynamic, according to time took by scrap procedure
     * @param ethical_delay Constant
     * @param startTime Time of scrap procedure start
     */
    protected void dynamicSleep(final long ethical_delay, final long startTime){

        // Sleep time is dynamic, according to time took by scrap procedure
        long delay = ethical_delay - (System.nanoTime()/1_000_000 - startTime/1_000_000);
//        System.out.println("Delay "+delay);
        if(delay > 0){
            sleep(delay);
        }
    }

    protected void statusPrint(final int interval, final int size) {
        printerCounter++;
        if ((printerCounter % interval) == 0) {
            System.out.println(printerCounter + "/" + size + "\n");
        }
    }

    /**
     * Sets class variable page.
     * @param link URL of the page
     * @return True if page was found and loaded successfully.
     */
    protected boolean loadPage(final String link){
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

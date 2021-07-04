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

    protected void printAndSleep(long delay, int size){
        printerCounter++;
        if ((printerCounter % 10) == 0)
            System.out.println(printerCounter + "/" + size);
        sleep(delay);
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

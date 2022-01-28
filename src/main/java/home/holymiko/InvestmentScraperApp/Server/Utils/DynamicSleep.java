package home.holymiko.InvestmentScraperApp.Server.Utils;

public class DynamicSleep {
    /**
     * Sleep time is dynamic, according to time took by scrap procedure
     * @param ethical_delay Constant
     * @param startTime Time of scrap procedure start
     */
    public static void dynamicSleep(final long ethical_delay, final long startTime){
        long delay = ethical_delay - (System.nanoTime()/1_000_000 - startTime/1_000_000);
        if(delay > 0){
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

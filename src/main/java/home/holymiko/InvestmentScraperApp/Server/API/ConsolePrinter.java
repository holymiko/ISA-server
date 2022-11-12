package home.holymiko.InvestmentScraperApp.Server.API;


import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ConsolePrinter {
    public static void statusPrint(final int interval, final int size, final int printerCounter) {
        if ((printerCounter % interval) == 0) {
            System.out.println(printerCounter + "/" + size + "\n");
        }
    }

    /**
     * Prints exchange rates.
     * Output includes date of record, iff it's NOT up to date
     * @param exchangeRateList to be printed
     */
    public static void printExchangeRates(final List<ExchangeRate> exchangeRateList) {
        // Get current date
        Date today = new Date(new java.util.Date().getTime());

        if(exchangeRateList == null || exchangeRateList.isEmpty()){
            return;
        }
        System.out.println();
        System.out.println("Exchange rates of Czech National Bank");
        System.out.println("-----------------------------------------------------------------");
        for (ExchangeRate exchangeRate : exchangeRateList) {
            if(exchangeRate == null) {
                continue;
            }
            System.out.format(
                    "%3s:     %10.3f      %10s\n",
                    exchangeRate.getCode(),
                    exchangeRate.getExchangeRate(),
                    // Prints Date only if it's NOT current
                    exchangeRate.getDate().toString().equals(today.toString()) ? "" : exchangeRate.getDate()
            );
        }
        System.out.println();
    }

    public static void printTimeStamp() {
        System.out.print(">> ");
        System.out.print(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date()));
        System.out.println(" <<");
    }

    private static Integer getPercentage(final double tickerStateCount, final double sum) {
        final DecimalFormat df = new DecimalFormat("###,###");
        return Math.round(
                Long.parseLong(
                        df.format(tickerStateCount*100/(sum))
                )
        );
    }

}

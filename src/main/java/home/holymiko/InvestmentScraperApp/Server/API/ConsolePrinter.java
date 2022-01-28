package home.holymiko.InvestmentScraperApp.Server.API;


import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.ExchangeRate;

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

    public static void printScrapStock(final String header, final Double ratingScore, final List<Double> ratings, final List<Double> results, final String currency) {
        System.out.println(header);
        for (Double x : ratings) {
            System.out.println(x);
        }
        System.out.println("Rating Score = "+ratingScore);
        for (Double x : results) {
            System.out.println(x);
        }
        System.out.println(currency);
        System.out.println();
    }

    /**
     * Prints short info about stock.
     * Consists only info of given parameters.
     * @param header
     * @param ratingScore
     * @param intrinsicValue
     * @param currency
     */
    public static void printScrapStockShort(final String header, final Double ratingScore, final Double intrinsicValue, final String currency) {
        System.out.println(header);
        System.out.println("  Rating Score = "+ratingScore);
        System.out.println("  Intrinsic Value = "+intrinsicValue);
        System.out.println("  "+currency);
        System.out.println();
    }

    public static void printTickerStatus(double good, double bad, double notfound, double newTickerCount) {
        final double sum = good + bad + notfound;
        final double totalTickers = sum + newTickerCount;

        System.out.println();
        System.out.println("Serenity Client");
        System.out.println("-----------------------------------------------------------------");
        System.out.format("Good:     %12d  %3d%s%n", Math.round(good), getPercentage(good, sum), "%");
        System.out.format("Bad:      %12d  %3d%s%n", Math.round(bad), getPercentage(bad, sum), "%");
        System.out.format("NotFound: %12d  %3d%s%n", Math.round(notfound), getPercentage(notfound, sum), "%");
        if(newTickerCount > 0 ) {
            System.out.format("New:      %12d  %3d%s%n", Math.round(newTickerCount), getPercentage(newTickerCount, sum), "%");
        }
        System.out.format("Total: %15d%n", Math.round(totalTickers));
        System.out.println();
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

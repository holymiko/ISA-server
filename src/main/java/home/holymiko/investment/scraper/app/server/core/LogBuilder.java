package home.holymiko.investment.scraper.app.server.core;


import home.holymiko.investment.scraper.app.server.type.entity.ExchangeRate;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogBuilder.class);

    public static void statusPrint(final int interval, final int size, final int printerCounter) {
        if ((printerCounter % interval) == 0) {
            LOGGER.info(printerCounter + "/" + size);
        }
    }

    public static void printScrapStock(final String header, final Double ratingScore, final List<Double> ratings, final List<Double> results, final String currency) {
        String variable = "\n";

        LOGGER.info(header);
        for (Double x : ratings) {
            variable += x + "\n";
        }
        LOGGER.info(variable);
        LOGGER.info("Rating Score = "+ratingScore);
        variable = "\n";
        for (Double x : results) {
            variable += x + "\n";
        }
        LOGGER.info(variable);
        LOGGER.info(currency + "\n");
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
        LOGGER.info(
                "\n" + header + "\n" +
                "  Rating Score = " + ratingScore + "\n" +
                "  Intrinsic Value = " + intrinsicValue + "\n" +
                "  " + currency
        );
    }

    public static void printTickerStatus(double good, double bad, double notfound, double newTickerCount) {
        final double sum = good + bad + notfound;
        final double totalTickers = sum + newTickerCount;

        LOGGER.info(
                "\nSerenity Scraper\n" +
                "-----------------------------------------------------------------\n" +
                String.format("Good:     %12d  %3d%s\n", Math.round(good), getPercentage(good, sum), "%") +
                String.format("Bad:      %12d  %3d%s\n", Math.round(bad), getPercentage(bad, sum), "%") +
                String.format("NotFound: %12d  %3d%s\n", Math.round(notfound), getPercentage(notfound, sum), "%") +
                (newTickerCount > 0 ? String.format("New:      %12d  %3d%s\n", Math.round(newTickerCount), getPercentage(newTickerCount, sum), "%") : "") +
                String.format("Total: %15d\n", Math.round(totalTickers))
        );
    }

    /**
     * Prints exchange rates.
     * Output includes date of record, iff it's NOT up to date
     * @param exchangeRateList to be printed
     */
    public static void printExchangeRates(final List<ExchangeRate> exchangeRateList) {
        // Get current date
        Date today = new Date(new java.util.Date().getTime());
        String exchangeRates = "";

        if(exchangeRateList == null || exchangeRateList.isEmpty()){
            return;
        }
        for (ExchangeRate exchangeRate : exchangeRateList) {
            if(exchangeRate == null) {
                continue;
            }
            exchangeRates += String.format(
                    "%3s:     %10.3f      %10s\n",
                    exchangeRate.getCode(),
                    exchangeRate.getExchangeRate(),
                    // Prints Date only if it's NOT current
                    exchangeRate.getDate().toString().equals(today.toString()) ? "" : exchangeRate.getDate()
            );
        }
        LOGGER.info(
                "\nExchange rates of Czech National Bank\n" +
                "-----------------------------------------------------------------\n" +
                exchangeRates
        );
    }

    public static void logTimeStamp() {
        LOGGER.info(
                ">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new java.util.Date()) + " <<"
        );
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

package home.holymiko.InvestmentScraperApp.Server.Utils;


import java.text.DecimalFormat;
import java.util.List;

public class ConsolePrinter {

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

    public static void printScrapStockShort(final String header, final Double ratingScore, final Double intrinsicValue, final String currency) {
        System.out.println(header);
        System.out.println("  Rating Score = "+ratingScore);
        System.out.println("  Intrinsic Value = "+intrinsicValue);
        System.out.println("  "+currency);
        System.out.println();
    }

    public static void printTickerStatus(double good, double bad, double notfound, double unknown) {
        final double sum = good + bad + notfound;
        final double totalTickers = sum + unknown;

        System.out.println();
        System.out.println("Serenity Scraper");
        System.out.println("-----------------------------------------------------------------");
        System.out.format("Good:     %12d  %3d%s%n", Math.round(good), printMethod(good, sum), "%");
        System.out.format("Bad:      %12d  %3d%s%n", Math.round(bad), printMethod(bad, sum), "%");
        System.out.format("NotFound: %12d  %3d%s%n", Math.round(notfound), printMethod(notfound, sum), "%");
        System.out.println(
                unknown > 0 ? "Unknown: " + Math.round(unknown) + "  " + printMethod(unknown, sum) + "%" : ""
        );
        System.out.format("Total: %15d%n", Math.round(totalTickers));
        System.out.println();
    }

    public static Integer printMethod(double tickerState, double sum) {
        final DecimalFormat df = new DecimalFormat("###,###");
        return Math.round(
                Long.parseLong(
                        df.format(tickerState*100/(sum))
                )
        );
    }

}

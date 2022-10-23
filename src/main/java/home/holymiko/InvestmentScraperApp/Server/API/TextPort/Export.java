package home.holymiko.InvestmentScraperApp.Server.API.TextPort;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Stock;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Export {

    public static final String TICKER_PATH = "txt/export/tickers/";
    private static final String STOCK_PATH = "txt/export/stocks/";
    private static final String DELIMITER = "#";

    ////// EXPORT

    public static void exportTickers(final List<Ticker> tickers) {
        System.out.println("Export tickers");
        try {
            final File file = new File(TICKER_PATH + LocalDate.now());
            file.mkdirs();
            final String location = file.getPath()+"/";
            final FileWriter goodWriter = new FileWriter(location+"good.txt");
            final FileWriter badWriter = new FileWriter(location+"bad.txt");
            final FileWriter notFoundWriter = new FileWriter(location+"notfound.txt");
            final FileWriter unknownWriter = new FileWriter(location+"unknown.txt");

            for (Ticker ticker : tickers) {
                String x = ticker.getTicker()+"\n";
                switch (ticker.getTickerState()) {
                    case GOOD -> goodWriter.write(x);
                    case BAD -> badWriter.write(x);
                    case NOTFOUND -> notFoundWriter.write(x);
                    case NEW -> unknownWriter.write(x);
                }
            }
            goodWriter.close();
            badWriter.close();
            notFoundWriter.close();
            unknownWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void exportStocks(final List<Stock> stocks) {
        System.out.println("Export stocks");
        try {
            final File file = new File(STOCK_PATH + LocalDate.now());
            file.mkdirs();
            final FileWriter stockWriter = new FileWriter(file.getPath()+"/stock.txt");

            for (Stock stock : stocks) {
                String x = stock.getName()+DELIMITER+
                        stock.getTicker().getTicker()+DELIMITER+
                        stock.getGrahamGrade()+DELIMITER+
                        stock.getCurrency()+DELIMITER+

                        stock.getRatingScore()+DELIMITER+
                        stock.getSizeInSales()+DELIMITER+
                        stock.getAssetsLiabilities()+DELIMITER+
                        stock.getNetAssetsLongTermDebt()+DELIMITER+
                        stock.getEarningsStability()+DELIMITER+
                        stock.getDividendRecord()+DELIMITER+
                        stock.getNcav()+DELIMITER+
                        stock.getEquityDebt()+DELIMITER+
                        stock.getSizeInAssets()+DELIMITER+

                        stock.getDefensivePrice()+DELIMITER+
                        stock.getEnterprisingPrice()+DELIMITER+
                        stock.getNcavPrice()+DELIMITER+
                        stock.getIntrinsicPrice()+DELIMITER+
                        stock.getPreviousClose()+DELIMITER+
                        stock.getIntrinsicValue()+"\n";
                stockWriter.write(x);
            }
            stockWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}

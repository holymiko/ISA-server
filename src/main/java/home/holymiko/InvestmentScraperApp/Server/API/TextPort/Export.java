package home.holymiko.InvestmentScraperApp.Server.API.TextPort;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Export {

    // TODO Move to .env (senior)
    private static final Logger LOGGER = LoggerFactory.getLogger(Export.class);
    public static final String TICKER_PATH = "txt/export/tickers/";
    private static final String STOCK_PATH = "txt/export/stocks/";
    private static final String DELIMITER = "#";

    ////// EXPORT

    public void exportToPDF(Product product) {
        // TODO Vyber knihovnu pro export
        //      Sestav a exportuj PDF soubor

        // TODO Remove this
        System.out.println("Data mock '" + product.getName() + "' has " + product.getPricePairs().size() + " prices");
    }

    public void exportToPDF(Portfolio portfolio) {
        // TODO Vyber knihovnu pro export
        //      Sestav a exportuj PDF soubor

        // TODO Remove this
        System.out.println("Portfolio includes " + portfolio.getInvestmentStocks().size() + " stocks and " + portfolio.getInvestmentMetals().size() + " precious metal investments");
    }


    public static void exportTickers(final List<Ticker> tickers) {
        LOGGER.info("Export tickers");
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
            LOGGER.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void exportStocks(final List<GrahamStock> grahamStocks) {
        LOGGER.info("Export grahamStocks");
        try {
            final File file = new File(STOCK_PATH + LocalDate.now());
            file.mkdirs();
            final FileWriter stockWriter = new FileWriter(file.getPath()+"/stock.txt");

            for (GrahamStock grahamStock : grahamStocks) {
                String x = grahamStock.getName()+DELIMITER+
                        grahamStock.getTicker().getTicker()+DELIMITER+
                        grahamStock.getGrahamGrade()+DELIMITER+
                        grahamStock.getCurrency()+DELIMITER+

                        grahamStock.getRatingScore()+DELIMITER+
                        grahamStock.getSizeInSales()+DELIMITER+
                        grahamStock.getAssetsLiabilities()+DELIMITER+
                        grahamStock.getNetAssetsLongTermDebt()+DELIMITER+
                        grahamStock.getEarningsStability()+DELIMITER+
                        grahamStock.getDividendRecord()+DELIMITER+
                        grahamStock.getNcav()+DELIMITER+
                        grahamStock.getEquityDebt()+DELIMITER+
                        grahamStock.getSizeInAssets()+DELIMITER+

                        grahamStock.getDefensivePrice()+DELIMITER+
                        grahamStock.getEnterprisingPrice()+DELIMITER+
                        grahamStock.getNcavPrice()+DELIMITER+
                        grahamStock.getIntrinsicPrice()+DELIMITER+
                        grahamStock.getPreviousClose()+DELIMITER+
                        grahamStock.getIntrinsicValue()+"\n";
                stockWriter.write(x);
            }
            stockWriter.close();
            LOGGER.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOGGER.error("An error occurred.");
            e.printStackTrace();
        }
    }

}

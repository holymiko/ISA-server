package home.holymiko.ScrapApp.Server.API;

import home.holymiko.ScrapApp.Server.Entity.Ticker;
import home.holymiko.ScrapApp.Server.Service.TickerService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Export {

    private final TickerService tickerService;

    public Export(TickerService tickerService) {
        this.tickerService = tickerService;
    }

    ////// EXPORT

    public void exportTickers() {
        try {
            final String location = "txt/export/";
            final FileWriter goodWriter = new FileWriter(location+"good.txt");
            final FileWriter badWriter = new FileWriter(location+"bad.txt");
            final FileWriter notFoundWriter = new FileWriter(location+"notfound.txt");
            final FileWriter unknownWriter = new FileWriter(location+"unknown.txt");
            final List<Ticker> tickers = this.tickerService.findAll();

            for (Ticker ticker : tickers) {
                String x = ticker.getTicker()+"\n";
                switch (ticker.getTickerState()) {
                    case GOOD -> goodWriter.write(x);
                    case BAD -> badWriter.write(x);
                    case NOTFOUND -> notFoundWriter.write(x);
                    case UNKNOWN -> unknownWriter.write(x);
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



}

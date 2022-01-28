package home.holymiko.InvestmentScraperApp.Server.Scraper.sources;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.API.TxtPort.Export;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;
import home.holymiko.InvestmentScraperApp.Server.Service.StockService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import home.holymiko.InvestmentScraperApp.Server.Utils.DynamicSleep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SerenityScraper extends Client {
    private static final double MIN_RATING_SCORE = 6.5;
    private static final long ETHICAL_DELAY = 1000;
    private static final String BASE_URL = "https://www.serenitystocks.com/stock/";

    private final TickerService tickerService;
    private final StockService stockService;

    @Autowired
    public SerenityScraper(TickerService tickerService, StockService stockService) {
        super();
        this.tickerService = tickerService;
        this.stockService = stockService;
    }

    //////////// PUBLIC

    public void tickersScrap(TickerState tickerState) {
        int counter = 0;
        Set<Ticker> tickers = this.tickerService.findByTickerState(tickerState);

        System.out.println("Trying to scrap " + tickerState + " tickers");
        tickerService.printTickerStatus();

        // Currency filter
        // TODO THis was not working for empty stock DB
//        tickers = filterTickersByCurrencies(tickers, new HashSet<>(Arrays.asList("USD", "EUR", "GBP", "CHF", "HKD")));

        for (Ticker ticker : tickers) {
            HtmlPage page;
            long startTime = System.nanoTime();

            // Page Not Found
            try {
                page = loadPage(BASE_URL + ticker.getTicker().toLowerCase(Locale.ROOT) );
            } catch (ResourceNotFoundException e) {
                // TODO If I lose Internet connection, Tickers in DB are false rewritten ?!
                this.tickerService.update(ticker, TickerState.NOTFOUND);
                System.out.println(">" + ticker.getTicker() + "<");
                counter++;
                ConsolePrinter.statusPrint(50, tickers.size(), counter);
                DynamicSleep.dynamicSleep(ETHICAL_DELAY, startTime);
                continue;
            }

            HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span");
            double ratingScore = Double.parseDouble( htmlElement.asText().replace("Rating Score = ", ""));

            if (ratingScore >= MIN_RATING_SCORE) {
                this.tickerService.update(ticker, TickerState.GOOD);
                try {
                    // TODO Clean this throwing
                    scrapStock(page, ticker, ratingScore);
                } catch (Exception ignored){}
            } else {
                this.stockService.deleteByTicker(ticker);
                this.tickerService.update(ticker, TickerState.BAD);
                System.out.println(">" + ticker.getTicker() + "< Bad");
            }
            counter++;
            ConsolePrinter.statusPrint(50, tickers.size(), counter);
            DynamicSleep.dynamicSleep(ETHICAL_DELAY, startTime);
        }
        tickerService.printTickerStatus();
        Export.exportTickers(tickerService.findAll());
        Export.exportStocks(stockService.findAll());
    }

    //////////// PRIVATE

    private void scrapStock(final HtmlPage page, final Ticker ticker, final Double ratingScore) {
        List<Double> ratings = new ArrayList<>();          // Graham Ratings
        List<Double> results = new ArrayList<>();          // Graham Results
        GrahamGrade grade = null;

        String header = ((HtmlElement) page.getFirstByXPath("/html/body/div[2]/div/section/h1")).asText();
        String currency = ((DomText) page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div[9]/div/div/text()")).asText();

        for(int i = 2; i <= 11; i++) {
            ratings.add(
                    Convert.numberConvertSerenity(
                            ((HtmlElement) page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div["+i+"]/div[2]/div"))
                                    .asText()
                    )
            );
        }
        for(int i = 2; i <= 8; i++) {
            HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div["+i+"]/div[2]/div");
            if(i == 5) {
                grade = Convert.gradeConvert(htmlElement.asText());
                continue;
            }
            results.add(
                    Convert.numberConvertSerenity(
                            htmlElement.asText()
                    )
            );
        }

//        printScrapStock(header, ratingScore, ratings, results, currency);
        ConsolePrinter.printScrapStockShort(header, ratingScore, results.get(5), currency);

        Stock stock = new Stock(
                header, ticker, grade, currency, ratingScore,
                ratings.get(0), ratings.get(1), ratings.get(2), ratings.get(3),
                ratings.get(4), ratings.get(5), ratings.get(6), ratings.get(7),
                results.get(0), results.get(1), results.get(2),
                results.get(3), results.get(4), results.get(5)
        );
        this.stockService.save(stock);
    }

    private Set<Ticker> filterTickersByCurrencies(Set<Ticker> tickers, Set<String> currencies) {
        return stockService.findByTickerState(tickers)
                .stream()
                .filter(
                        stock -> currencies.contains( stock.getCurrency() )
                ).map(
                        Stock::getTicker
                ).collect(Collectors.toSet());
    }

    /**
     * In past used for modification of NEW Tickers
     * @param target
     */
    private void fixer(String target) {
        Set<Ticker> tickers = this.tickerService.findByTickerState(TickerState.NOTFOUND);
        int i = 0;
        for (Ticker ticker: tickers) {
            if(ticker.getTicker().contains(target)) {
//                this.tickerService.delete(ticker);
//                this.tickerService.updateTicker(ticker, TickerState.NOTFOUND);
                String name = ticker.getTicker().replace(target, "");
                if( this.tickerService.save(name) ){
                    i++;
                }
            }
        }
        System.out.println("Total saved: "+i);
    }

}

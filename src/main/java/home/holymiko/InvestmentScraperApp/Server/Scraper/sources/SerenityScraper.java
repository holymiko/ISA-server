package home.holymiko.InvestmentScraperApp.Server.Scraper.sources;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.InvestmentScraperApp.Server.API.Port.Export;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Extract;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Scraper;
import home.holymiko.InvestmentScraperApp.Server.Service.StockService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Utils.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SerenityScraper extends Scraper {
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

    public void run() {
//        printSerenityStatus();
//        Export.exportTickers(tickerService.findAll());
//        Export.exportStocks(stockService.findAll());
//        tickersScrap(TickerState.GOOD);
//        fixer("$");
    }

    public void tickersScrap(TickerState tickerState) {
        System.out.println("Trying to scrap " + tickerState + " tickers");
        ConsolePrinter.printTickerStatus(
                tickerService.findByTickerState(TickerState.GOOD).size(),
                tickerService.findByTickerState(TickerState.BAD).size(),
                tickerService.findByTickerState(TickerState.NOTFOUND).size(),
                tickerService.findByTickerState(TickerState.NEW).size()
        );

        Set<Ticker> tickers = this.tickerService.findByTickerState(tickerState);

        // Currency filter
        tickers = filterTickersByCurrencies(tickers, new HashSet<>(Arrays.asList("USD", "EUR", "GBP", "CHF", "HKD")));

        for (Ticker ticker : tickers) {
            long startTime = System.nanoTime();

            if( !loadPage(BASE_URL + ticker.getTicker().toLowerCase(Locale.ROOT) )) {
                this.tickerService.update(ticker, TickerState.NOTFOUND);
                System.out.println(">" + ticker.getTicker() + "<");
            } else {
                HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span");
                double ratingScore = Double.parseDouble( htmlElement.asText().replace("Rating Score = ", ""));

                if (ratingScore >= MIN_RATING_SCORE) {
                    this.tickerService.update(ticker, TickerState.GOOD);
                    stockScrap(ticker, ratingScore);
                } else {
                    this.stockService.deleteByTicker(ticker);
                    this.tickerService.update(ticker, TickerState.BAD);
                    System.out.println(">" + ticker.getTicker() + "< Bad");
                }
            }
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, startTime, 50, tickers.size());
        }
        printerCounter = 0;
        tickerService.printTickerStatus();
        Export.exportTickers(tickerService.findAll());
        Export.exportStocks(stockService.findAll());
    }

    //////////// PRIVATE

    private void stockScrap(final Ticker ticker, final Double ratingScore) {
        List<Double> ratings = new ArrayList<>();          // Graham Ratings
        List<Double> results = new ArrayList<>();          // Graham Results
        GrahamGrade grade = null;

        String header = ((HtmlElement) page.getFirstByXPath("/html/body/div[2]/div/section/h1")).asText();
        String currency = ((DomText) page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div[9]/div/div/text()")).asText();

        for(int i = 2; i <= 11; i++) {
            ratings.add(
                    Extract.numberExtractSerenity(
                            ((HtmlElement) page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div["+i+"]/div[2]/div"))
                                    .asText()
                    )
            );
        }
        for(int i = 2; i <= 8; i++) {
            HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div["+i+"]/div[2]/div");
            if(i == 5) {
                grade = Extract.gradeConvert(htmlElement);
                continue;
            }
            results.add(
                    Extract.numberExtractSerenity(
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

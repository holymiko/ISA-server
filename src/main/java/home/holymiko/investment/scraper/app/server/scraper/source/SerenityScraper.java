package home.holymiko.investment.scraper.app.server.scraper.source;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.investment.scraper.app.server.api.file.port.Export;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.type.enums.GrahamGrade;
import home.holymiko.investment.scraper.app.server.type.enums.TickerState;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Convert;
import home.holymiko.investment.scraper.app.server.service.StockGrahamService;
import home.holymiko.investment.scraper.app.server.service.TickerService;
import home.holymiko.investment.scraper.app.server.core.LogBuilder;
import home.holymiko.investment.scraper.app.server.type.entity.StockGraham;
import home.holymiko.investment.scraper.app.server.type.entity.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SerenityScraper extends Client implements SerenityScraperInterface {
    private static final double MIN_RATING_SCORE = 6.5;
    private static final long ETHICAL_DELAY = 1000;
    private static final String BASE_URL = "https://www.grahamvalue.com/stock/";
    private static final Logger LOGGER = LoggerFactory.getLogger(SerenityScraper.class);

    private final TickerService tickerService;
    private final StockGrahamService stockGrahamService;

    @Autowired
    public SerenityScraper(TickerService tickerService, StockGrahamService stockGrahamService) {
        super("SerenityScraper");
        this.tickerService = tickerService;
        this.stockGrahamService = stockGrahamService;
    }

    //////////// PUBLIC

    public void tickersScrap(TickerState tickerState) {
        int counter = 0;
        Set<Ticker> tickers = this.tickerService.findByTickerState(tickerState);

        LOGGER.info("Trying to scrap " + tickerState + " tickers");
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
                LOGGER.info(ticker.getTicker() + " - not found");
                counter++;
                LogBuilder.statusPrint(50, tickers.size(), counter);
                dynamicSleep(ETHICAL_DELAY, startTime);
                continue;
            }

            double ratingScore = Double.parseDouble(
                    scrapRatingScore(page).replace("Rating Score = ", "")
            );

            if (ratingScore >= MIN_RATING_SCORE) {
                this.tickerService.update(ticker, TickerState.GOOD);
                try {
                    // TODO Clean this throwing
                    scrapStock(page, ticker, ratingScore);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.stockGrahamService.deleteByTicker(ticker);
                this.tickerService.update(ticker, TickerState.BAD);
                LOGGER.info(ticker.getTicker() + " - Bad");
            }
            counter++;
            LogBuilder.statusPrint(50, tickers.size(), counter);
            dynamicSleep(ETHICAL_DELAY, startTime);
        }
        tickerService.printTickerStatus();
        Export.exportTickers(tickerService.findAll());
        Export.exportStocks(stockGrahamService.findAll());
    }

    //////////// PRIVATE

    private void scrapStock(final HtmlPage page, final Ticker ticker, final Double ratingScore) {
        List<Double> ratings = new ArrayList<>();          // Graham Ratings
        List<Double> results = new ArrayList<>();          // Graham Results
        GrahamGrade grade = null;

        String header = scrapHeader(page);
        String currency = scrapCurrency(page);

        for(int i = 2; i <= 11; i++) {
            ratings.add(
                    Convert.serenityToNumber(
                            getRating(page, i)
                    )
            );
        }
        for(int i = 2; i <= 8; i++) {
            String htmlElement = getResult(page, i);
            if(i == 5) {
                grade = Convert.grahamGrade(htmlElement);
                continue;
            }
            results.add(
                    Convert.serenityToNumber(
                            htmlElement
                    )
            );
        }

        LogBuilder.printScrapStockShort(header, ratingScore, results.get(5), currency);

        StockGraham stockGraham = new StockGraham(
                new Date(), header, ticker, grade, currency, ratingScore,
                ratings.get(0), ratings.get(1), ratings.get(2), ratings.get(3),
                ratings.get(4), ratings.get(5), ratings.get(6), ratings.get(7),
                ratings.get(8), ratings.get(9),
                results.get(0), results.get(1), results.get(2),
                results.get(3), results.get(4), results.get(5)
        );
        this.stockGrahamService.save(stockGraham);
    }

    private Set<Ticker> filterTickersByCurrencies(Set<Ticker> tickers, Set<String> currencies) {
        return stockGrahamService.findByTicker(tickers)
                .stream()
                .filter(
                        stock -> currencies.contains( stock.getCurrency() )
                ).map(
                        StockGraham::getTicker
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
        LOGGER.info("Total saved: "+i);
    }

}

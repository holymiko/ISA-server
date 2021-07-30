package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.ScrapApp.Server.Entity.Enum.*;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Service.StockService;
import home.holymiko.ScrapApp.Server.Service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.*;

@Component
public class ScrapSerenity extends Scrap {
    private static final double MIN_RATING_SCORE = 6.5;
    private static final long ETHICAL_DELAY = 700;
    private static final String BASE_URL = "https://www.serenitystocks.com/stock/";

    private final TickerService tickerService;
    private final StockService stockService;

    @Autowired
    public ScrapSerenity(TickerService tickerService, StockService stockService) {
        super();
        this.tickerService = tickerService;
        this.stockService = stockService;

        run();
    }


    public void run() {
        printTotalStatus();
//        tickerService.exportTickers();
//        tickersScrap(TickerState.GOOD);
//        tickerService.loadTickers();
//        fixer("$");
    }

    public void tickersScrap(TickerState tickerState) {
        System.out.println("Trying to scrap "+tickerState+" tickers");
        printTotalStatus();

        List<Ticker> tickers = this.tickerService.findByTickerState(tickerState);

        for (Ticker ticker : tickers) {
            long start = System.nanoTime();
            if( !loadPage(BASE_URL + ticker.getTicker().toLowerCase(Locale.ROOT) )) {
                this.tickerService.update(ticker, TickerState.NOTFOUND);
                System.out.println(">"+ticker.getTicker()+"<");
            }
            else {
                HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span");
                double ratingScore = Double.parseDouble( htmlElement.asText().replace("Rating Score = ", ""));
                if (ratingScore >= MIN_RATING_SCORE) {
                    this.tickerService.update(ticker, TickerState.GOOD);
                    stockScrap(ticker, ratingScore);
                } else {
                    this.stockService.deleteByTicker(ticker);
                    this.tickerService.update(ticker, TickerState.BAD);
                    System.out.println(">"+ticker.getTicker()+"< Bad");
                }
            }
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, start, tickers.size());
        }
        printerCounter = 0;
        printTotalStatus();
    }

    public void stockScrap(final Ticker ticker, final Double ratingScore) {
        List<Double> ratings = new ArrayList<>();          // Graham Ratings
        List<Double> results = new ArrayList<>();          // Graham Results
        GrahamGrade grade = null;

        String header = ((HtmlElement) page.getFirstByXPath("/html/body/div[2]/div/section/h1")).asText();
        String currency = ((DomText) page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div[9]/div/div/text()")).asText();

        for(int i = 2; i <= 11; i++) {
            ratings.add( Extractor.serenityElementToDouble( page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div["+i+"]/div[2]/div")) );
        }
        for(int i = 2; i <= 8; i++) {
            HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div["+i+"]/div[2]/div");
            if(i == 5) {
                grade = Extractor.gradeExtractor(htmlElement);
                continue;
            }
            results.add( Extractor.serenityElementToDouble(htmlElement) );
        }

//        printScrapStock(header, ratingScore, ratings, results, currency);
        printScrapStockShort(header, ratingScore, results.get(5), currency);

        Stock stock = new Stock(
                header, ticker, grade, currency, ratingScore,
                ratings.get(0), ratings.get(1), ratings.get(2), ratings.get(3),
                ratings.get(4), ratings.get(5), ratings.get(6), ratings.get(7),
                results.get(0), results.get(1), results.get(2),
                results.get(3), results.get(4), results.get(5)
        );
        this.stockService.save(stock);
    }

    private void fixer(String target) {
        List<Ticker> tickers = this.tickerService.findByTickerState(TickerState.NOTFOUND);
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

    private static void printScrapStock(final String header, final Double ratingScore, final List<Double> ratings, final List<Double> results, final String currency) {
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

    private static void printScrapStockShort(final String header, final Double ratingScore, final Double intrinsicValue, final String currency) {
        System.out.println(header);
        System.out.println("  Rating Score = "+ratingScore);
        System.out.println("  Intrinsic Value = "+intrinsicValue);
        System.out.println("  "+currency);
        System.out.println();
    }

    private void printTotalStatus() {
        final double total = tickerService.findAll().size();
        final double good = tickerService.findByTickerState(TickerState.GOOD).size();
        final double bad = tickerService.findByTickerState(TickerState.BAD).size();
        final double notfound = tickerService.findByTickerState(TickerState.NOTFOUND).size();
        final double unknown = tickerService.findByTickerState(TickerState.UNKNOWN).size();
        final DecimalFormat df = new DecimalFormat("###.###");

        System.out.println();
        System.out.println("Good: "+Math.round(good)+"  "+df.format(good*100/(notfound+bad+good))+"%");
        System.out.println("Bad: "+Math.round(bad)+"  "+df.format(bad*100/(notfound+bad+good))+"%");
        System.out.println("NotFound: "+Math.round(notfound)+"  "+df.format(notfound*100/(notfound+bad+good))+"%");
        System.out.println();
        System.out.println("Unknown: "+Math.round(unknown)+"  "+df.format(unknown*100/total)+"%");
        System.out.println("Total: "+Math.round(total));
        System.out.println();
    }

}

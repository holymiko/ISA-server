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
    private static final long MIN_TICKER_LENGTH = 4;
    private static final long MAX_TICKER_LENGTH = 50;
    private static final double MAX_RATING_SCORE = 6.5;
    private static final long DELAY = 700;
    private static final String BASE_URL = "https://www.serenitystocks.com/stock/";
    private final TickerService tickerService;
    private final StockService stockService;

    @Autowired
    public ScrapSerenity(TickerService tickerService, StockService stockService) {
        super();
        this.tickerService = tickerService;
        this.stockService = stockService;

        sTickers();
    }


    public void sTickers() {
        status();
        scrapTicker();
//        readFile3("txt/YahooStockTickers.txt");
//        status();
//        fixer("$");
//        fixer(".");
//        fixer("-");
//        status();
    }

    private void generator() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
        int i = 0;
//        for (char character1 : alphabet) {
            for (char character2 : alphabet) {
                for (char character3 : alphabet) {
                    String name = "" +/* character1 +*/ character2+character3;
                    if (this.tickerService.optionalSave(name))
                        i++;
                }
            }
//        }
        System.out.println("Generated: "+i);
    }

    private void fixer(String target) {
        List<Ticker> tickers = this.tickerService.findByTickerState(TickerState.NOTFOUND);
        for (Ticker ticker: tickers) {
            if(ticker.getTicker().contains(target)) {
                String name = ticker.getTicker().replace(target, "");
                this.tickerService.optionalSave(name);
            }
        }
    }

    public void status() {
        double total = tickerService.findAll().size();
        double good = tickerService.findByTickerState(TickerState.GOOD).size();
        double bad = tickerService.findByTickerState(TickerState.BAD).size();
        double notfound = tickerService.findByTickerState(TickerState.NOTFOUND).size();
        double unknown = tickerService.findByTickerState(TickerState.UNKNOWN).size();
        DecimalFormat df = new DecimalFormat("###.###");

        System.out.println();
        System.out.println("Good: "+Math.round(good)+"  "+df.format(good*100/(notfound+bad+good))+"%");
        System.out.println("Bad: "+Math.round(bad)+"  "+df.format(bad*100/(notfound+bad+good))+"%");
        System.out.println("NotFound: "+Math.round(notfound)+"  "+df.format(notfound*100/(notfound+bad+good))+"%");
        System.out.println();
        System.out.println("Unknown: "+Math.round(unknown)+"  "+df.format(unknown*100/total)+"%");
        System.out.println("Total: "+Math.round(total));
        System.out.println();
    }

    public void scrapTicker() {
        List<Ticker> tickerList = this.tickerService.findByTickerState(TickerState.UNKNOWN);
        int i = 0;
        for (Ticker ticker: tickerList) {
            if(ticker.getTicker().length() < MIN_TICKER_LENGTH || ticker.getTicker().length() > MAX_TICKER_LENGTH){
                continue;
            }
            if(ticker.getTicker().contains(".")){
                continue;
            }
            if( !loadPage(BASE_URL+ticker.getTicker().toLowerCase(Locale.ROOT) )) {
                this.tickerService.updateTicker(ticker, TickerState.NOTFOUND);
                System.out.println(">"+ticker.getTicker()+"<");
            } else {
                HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span");
                if (Double.parseDouble(htmlElement.asText().split(" = ")[1]) >= MAX_RATING_SCORE) {
                    this.tickerService.updateTicker(ticker, TickerState.GOOD);
                    scrapStock(ticker);
                } else {
                    this.tickerService.updateTicker(ticker, TickerState.BAD);
                    System.out.println(">"+ticker.getTicker()+"< Bad");
                }
            }

            sleep(DELAY);

            i++;
            if(i >= 50){
                i = 0;
                status();
            }
        }
        status();
    }

    public void scrapStock(Ticker ticker) {
        HtmlElement headerElement = page.getFirstByXPath("/html/body/div[2]/div/section/h1");
        List<HtmlElement> htmlElementList = new ArrayList<>();          // Graham Ratings
        List<HtmlElement> htmlElementList2 = new ArrayList<>();         // Graham Result

        for(int i = 2; i <= 11; i++) {
            htmlElementList.add(page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div["+i+"]/div[2]/div"));
        }
        HtmlElement ratingScore = page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span");

        for(int i = 2; i <= 4; i++) {
            htmlElementList2.add(page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div["+i+"]/div[2]/div"));
        }
        HtmlElement htmlGrade = page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div[5]/div[2]/div");
        for(int i = 6; i <= 8; i++) {
            htmlElementList2.add(page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div["+i+"]/div[2]/div"));
        }
        DomText currency = page.getFirstByXPath("//*[@id=\"bootstrap-panel-2-body\"]/div[9]/div/div/text()");


        List<Double> doubles = new ArrayList<>();          // Graham Ratings
        List<Double> doubles1 = new ArrayList<>();         // Graham Result
        for (HtmlElement element: htmlElementList) {
            doubles.add( Double.parseDouble( formatString(element) ));
        }
        for (HtmlElement element: htmlElementList2) {
            doubles1.add( Double.parseDouble( formatString(element) ));
        }

        String header = headerElement.asText();
        Double rating = Double.parseDouble( ratingScore.asText().replace("Rating Score = ", ""));

        System.out.println(header);
        for (Double x: doubles) {
            System.out.println(x);
        }
        System.out.println("Rating Score = "+rating);
        for (Double x: doubles1) {
            System.out.println(x);
        }
        System.out.println(currency.asText()+'\n');

        Stock stock = new Stock(
                header, ticker, formatGrade(htmlGrade), currency.asText(), rating,
                doubles.get(0), doubles.get(1), doubles.get(2), doubles.get(3),
                doubles.get(4), doubles.get(5), doubles.get(6), doubles.get(7),
                doubles1.get(0), doubles1.get(1), doubles1.get(2), doubles1.get(3), doubles1.get(4), doubles1.get(5)
        );
        this.stockService.save(stock);
    }

    public String formatString(HtmlElement element) {
        String result = element.asText();
        result = result.replace("%", "");
        result = result.replace(",", "");
        return result;
    }

    public GrahamGrade formatGrade(HtmlElement element) {
        switch (element.asText().toLowerCase(Locale.ROOT)) {
            case "enterprising" -> { return GrahamGrade.ENTERPRISING; }
            case "defensive" -> { return GrahamGrade.DEFENSIVE; }
            case "ungraded" -> { return GrahamGrade.UNGRADED; }
            case "ncav" -> { return GrahamGrade.NCAV; }
            default -> { return GrahamGrade.UNKNOWN; }
        }
    }

}

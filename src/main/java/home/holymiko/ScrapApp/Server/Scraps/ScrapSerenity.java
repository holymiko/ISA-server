package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.ScrapApp.Server.Entity.Enum.*;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Repository.StockRepository;
import home.holymiko.ScrapApp.Server.Repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Component
public class ScrapSerenity {
    private static final long MIN_TICKER_LENGTH = 3;
    private static final double MAX_RATING_SCORE = 6.5;
    private static final long DELAY = 1000;
    private HtmlPage page;
    private final WebClient client;
    private static final String BASE_URL = "https://www.serenitystocks.com/stock/";
    private final TickerRepository tickerRepository;
    private final StockRepository stockRepository;

    @Autowired
    public ScrapSerenity(TickerRepository tickerRepository, StockRepository stockRepository) {
        this.tickerRepository = tickerRepository;
        this.stockRepository = stockRepository;
        client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        sTickers();
    }

    /////// PRODUCT

    public void sTickers() {
//        readFile("nasdaqtraded.txt");
//        readFile("nasdaqlisted.txt");
        readFile("otherlisted.txt");

        status();
//        scrap();
//        status();
    }

    public void readFile(String fileName) {
        int a;
        int b;
        switch (fileName) {
            case "nasdaqtraded.txt":
                a = 1;
                b = 5;
                break;
            case "nasdaqlisted.txt":
                a = 0;
                b = 6;
                break;
            case "otherlisted.txt":
                a = 0;
                b = 4;
                break;
            default:
                return;
        }
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("\\|");
                if( data.length >= 6 && !data[b].equals("Y") ) {            // No ETFs
                    if( tickerRepository.findById(data[a]).isEmpty() ) {
                        tickerRepository.save(new Ticker(data[a], TickerState.UNKNOWN));
                        System.out.println("Saved new - "+data[a]);
                    } else {
                        System.out.println("Already known - "+data[a]);
                    }
                    i++;
                } else {
                    System.out.println("Kicked");
                }
            }
            System.out.println("Total: "+i);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void status() {
        System.out.println("Celkem: "+tickerRepository.findAll().size());
        System.out.println("Unknown: "+tickerRepository.findByTickerState(TickerState.UNKNOWN).size());
        System.out.println("NotFound: "+tickerRepository.findByTickerState(TickerState.NOTFOUND).size());
        System.out.println("Bad: "+tickerRepository.findByTickerState(TickerState.BAD).size());
        System.out.println("Good: "+tickerRepository.findByTickerState(TickerState.GOOD).size());
    }

    public void scrap() {
        List<Ticker> tickerList = this.tickerRepository.findByTickerState(TickerState.UNKNOWN);
        for (Ticker ticker: tickerList) {
            if( !loadPage(BASE_URL+ticker.getTicker().toLowerCase(Locale.ROOT) )) {
                updateTicker(ticker, TickerState.NOTFOUND);
            } else {
                HtmlElement htmlElement = page.getFirstByXPath("//*[@id=\"bootstrap-panel-body\"]/div[12]/div/div/h3/span");
                if (Double.parseDouble(htmlElement.asText().split(" = ")[1]) >= MAX_RATING_SCORE) {
                    updateTicker(ticker, TickerState.GOOD);
                    saveStock(ticker);
                } else {
                    updateTicker(ticker, TickerState.BAD);
                }
            }

            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveStock(Ticker ticker) {
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
            doubles.add( Double.parseDouble( format(element) ));
        }
        for (HtmlElement element: htmlElementList2) {
            doubles1.add( Double.parseDouble( format(element) ));
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
        this.stockRepository.save(stock);
    }

    @Transactional
    public void updateTicker(Ticker ticker, TickerState tickerState) {
//        System.out.println("Update ticker");
        ticker.setTickerState(tickerState);
        tickerRepository.save(ticker);
    }

    public String format(HtmlElement element) {
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

    private boolean loadPage(String link){
        page = null;
        try {
            page = client.getPage(link);                // Product page
        } catch (Exception e)  {
//            e.printStackTrace();
            return false;
        }
        return true;
    }
}

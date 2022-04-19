package home.holymiko.InvestmentScraperApp.Server.Scraper;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface ScrapRedemptionFromListInterface {

    List<Pair<String, Double>> scrapRedemptionFromList();
}

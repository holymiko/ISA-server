package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import org.springframework.data.util.Pair;

import java.util.List;

public interface RedemptionListInterface {

    List<Pair<String, Double>> scrapRedemptionFromList();
}

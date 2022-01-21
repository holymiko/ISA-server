package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.EnumKnown.Enum.Metal;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class ScrapHistory {
    private static final long MINUTES_DELAY = 15;
    private static final long YEAR_DELAY = 1000L;

    private LocalDateTime lastAllProducts = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastGold = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastSilver = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPlatinum = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPalladium = LocalDateTime.now().minusYears(YEAR_DELAY);

    private LocalDateTime lastAllLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastGoldLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastSilverLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPlatinumLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPalladiumLinks = LocalDateTime.now().minusYears(YEAR_DELAY);

    private boolean isRunning = false;

    private List<Pair<Metal, LocalDateTime>> latestScrapHistory;

}
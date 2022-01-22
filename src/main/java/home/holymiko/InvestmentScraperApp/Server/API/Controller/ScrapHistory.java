package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapRefusedException;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.EnumKnown.Enum.Metal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScrapHistory {
    private static final long MINUTES_DELAY = 15;
    private static final long YEAR_DELAY = 1000L;

    private static LocalDateTime lastAllProducts = LocalDateTime.now().minusYears(YEAR_DELAY);
    private static LocalDateTime lastAllLinks = LocalDateTime.now().minusYears(YEAR_DELAY);

    private static boolean isRunning = false;

    private final Map< Metal, LocalDateTime> scrapHistory;
    private final Map< Metal, LocalDateTime> scrapLinkHistory;

    @Autowired
    public ScrapHistory() {
                scrapHistory = new ArrayList<Metal>(EnumSet.allOf(Metal.class))
                        .stream()
                        .map(
                                this::init
                        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                scrapLinkHistory = new ArrayList<Metal>(EnumSet.allOf(Metal.class))
                        .stream()
                        .map(
                                this::init
                        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private AbstractMap.SimpleEntry<Metal, LocalDateTime> init(Metal metal) {
        return new AbstractMap.SimpleEntry<>(metal, LocalDateTime.now().minusYears(YEAR_DELAY));
    }


    public static void frequencyHandlingAll() throws ScrapRefusedException {
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllLinks)
        || LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllProducts) ) {
            isRunning = false;
            throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
        }
    }

    public static void frequencyHandlingAll(boolean links) throws ScrapRefusedException {
        if (links) {
            if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllLinks) ) {
                isRunning = false;
                throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
            }
        } else {
            if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllProducts) ) {
                isRunning = false;
                throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
            }
        }


    }

    public void frequencyHandling(Metal metal) throws ScrapRefusedException {
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(scrapHistory.get(metal)) ) {
            isRunning = false;
            throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
        }
    }

    public static void timeUpdate() {
        lastAllLinks = LocalDateTime.now();
        lastAllProducts = LocalDateTime.now();
    }

    public static void timeUpdate(boolean links) {
        if (links) {
            lastAllLinks = LocalDateTime.now();
        } else {
            lastAllProducts = LocalDateTime.now();
        }
    }

    public void timeUpdateLink(Metal metal) {
        scrapLinkHistory.put(metal, LocalDateTime.now());
    }

    public void timeUpdate(Metal metal) {
        scrapHistory.put(metal, LocalDateTime.now());
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setIsRunning(boolean isRunning) {
        ScrapHistory.isRunning = isRunning;
    }
}
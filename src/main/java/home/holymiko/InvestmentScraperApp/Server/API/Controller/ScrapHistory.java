package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapRefusedException;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
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
    private final Map< Dealer, LocalDateTime> scrapDealerHistory;

    @Autowired
    public ScrapHistory() {
        scrapHistory = new ArrayList<>(EnumSet.allOf(Metal.class))
                .stream()
                .map(
                        this::init
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        scrapLinkHistory = new ArrayList<>(EnumSet.allOf(Metal.class))
                .stream()
                .map(
                        this::init
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        scrapDealerHistory = new ArrayList<>(EnumSet.allOf(Dealer.class))
                .stream()
                .map(
                        this::init
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private AbstractMap.SimpleEntry<Metal, LocalDateTime> init(Metal metal) {
        return new AbstractMap.SimpleEntry<>(metal, LocalDateTime.now().minusYears(YEAR_DELAY));
    }

    private AbstractMap.SimpleEntry<Dealer, LocalDateTime> init(Dealer dealer) {
        return new AbstractMap.SimpleEntry<>(dealer, LocalDateTime.now().minusYears(YEAR_DELAY));
    }



    ////// FREQUENCY_HANDLING

    /**
     * Supposed to be used before start of the Run.
     * Doesn't turn off isRunning
     * @throws ScrapRefusedException
     */
    public static void frequencyHandlingAll() throws ScrapRefusedException {
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllLinks)
        || LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllProducts) ) {
            throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
        }
    }

    /**
     * Supposed to be used before start of the Run.
     * Doesn't turn off isRunning
     * @param links
     * @throws ScrapRefusedException
     */
    public static void frequencyHandlingAll(boolean links) throws ScrapRefusedException {
        if (links) {
            if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllLinks) ) {
                throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
            }
        } else {
            if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(lastAllProducts) ) {
                throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
            }
        }
    }

    public void frequencyHandling(Metal metal) throws ScrapRefusedException {
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(scrapHistory.get(metal)) ) {
            throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
        }
    }

    public void frequencyHandling(Dealer dealer) throws ScrapRefusedException {
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(scrapDealerHistory.get(dealer)) ) {
            throw new ScrapRefusedException( "Updated less then " + MINUTES_DELAY + " minutes ago");
        }
    }


    ////// TIME_UPDATE

    public static void timeUpdate(boolean links, boolean products) {
        if (links) {
            lastAllLinks = LocalDateTime.now();
        }
        if (products) {
            lastAllProducts = LocalDateTime.now();
        }
    }

    public void timeUpdateLink(Metal metal) {
        scrapLinkHistory.put(metal, LocalDateTime.now());
    }

    public void timeUpdate(Dealer dealer) {
        scrapDealerHistory.put(dealer, LocalDateTime.now());
    }

    public void timeUpdate(Metal metal) {
        scrapHistory.put(metal, LocalDateTime.now());
    }


    ////// RUNNING

    public static void isRunning() throws ScrapRefusedException {
        if(isRunning) {
            System.out.println("ScrapController - IM_IN_USE");
            throw new ScrapRefusedException();
        }
    }

    public static void startRunning() throws ScrapRefusedException {
        isRunning();
        ScrapHistory.isRunning = true;
    }

    public static void stopRunning() {
        ScrapHistory.isRunning = false;
    }
}
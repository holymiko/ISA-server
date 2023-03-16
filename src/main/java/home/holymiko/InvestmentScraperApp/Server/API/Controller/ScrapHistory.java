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

    private final Map<Metal, LocalDateTime> scrapHistory;

    @Autowired
    public ScrapHistory() {
        scrapHistory = new ArrayList<>(EnumSet.allOf(Metal.class))
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
        if(metal == null) {
            return;
        }
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(scrapHistory.get(metal)) ) {
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

    public void timeUpdate(Metal metal) {
        scrapHistory.put(metal, LocalDateTime.now());
    }


    ////// RUNNING

    private static void isRunning() throws ScrapRefusedException {
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
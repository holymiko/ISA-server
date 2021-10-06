package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Entity.CurrencyRatio;
import home.holymiko.InvestmentScraperApp.Server.Entity.InvestmentMetal;
import home.holymiko.InvestmentScraperApp.Server.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Repository.CurrencyRatioRepository;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.google.currency.EurCzkScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.google.metal.GoldScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyService {

//    private final GoldScraper goldScraper;
    private final EurCzkScraper eurCzkScraper;
    private final CurrencyRatioRepository currencyRatioRepository;

    private static final double TROY_OUNCE = 31.1034768;

    @Autowired
    public CurrencyService(EurCzkScraper eurCzkScraper, CurrencyRatioRepository currencyRatioRepository) {
        this.eurCzkScraper = eurCzkScraper;
        this.currencyRatioRepository = currencyRatioRepository;
    }

    @Transactional
    public CurrencyRatio save() throws ResponseStatusException {
        System.out.println("CurrencyRatio.save");
        return currencyRatioRepository.save(eurCzkScraper.scrap());
//        return currencyRatioRepository.findById(investmentMetal.getId()).get();
    }

}


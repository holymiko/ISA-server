package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.PriceRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final PricePairRepository pricePairRepository;
    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PricePairRepository pricePairRepository, PriceRepository priceRepository) {
        this.pricePairRepository = pricePairRepository;
        this.priceRepository = priceRepository;
    }

    public PricePair save(PricePair pricePair) {
        return this.pricePairRepository.save(pricePair);
    }

    public Price save(Price price) {
        return this.priceRepository.save(price);
    }
}

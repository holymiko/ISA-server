package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Entity.ExchangeRate;
import home.holymiko.InvestmentScraperApp.Server.Repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Transactional
    public ExchangeRate save(ExchangeRate exchangeRate) throws ResponseStatusException {
        System.out.println("CurrencyRatio.save");
        return exchangeRateRepository.save(exchangeRate);
    }

}


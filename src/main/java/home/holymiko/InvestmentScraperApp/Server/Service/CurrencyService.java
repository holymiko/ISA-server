package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;


@Service
public class CurrencyService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public CurrencyService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Transactional
    public ExchangeRate save(ExchangeRate exchangeRate) throws ResponseStatusException {
        delete(exchangeRate.getCode(), exchangeRate.getDate());
        return exchangeRateRepository.save(exchangeRate);
    }

    public ExchangeRate findExchangeRate(String code) {
        return exchangeRateRepository.findFirstByCodeOrderByDateDesc(code);
    }

    @Transactional
    public void delete(String code, Date date) {
        exchangeRateRepository.deleteByCodeAndDate(code, date);
    }


}


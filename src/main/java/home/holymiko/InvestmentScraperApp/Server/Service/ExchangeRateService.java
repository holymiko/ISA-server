package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.ExchangeRate;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ExchangeRateRepository;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Arrays;


@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Transactional
    public ExchangeRate save(ExchangeRate exchangeRate) throws ResponseStatusException {
//        System.out.println("CurrencyRatio.save");
        return exchangeRateRepository.save(exchangeRate);
    }

    public ExchangeRate findFirstByCodeOrderByDateDesc(String currencySignature) {
        return exchangeRateRepository.findFirstByCodeOrderByDateDesc(currencySignature);
    }

    @Transactional
    public void delete(String code, Date date) {
        exchangeRateRepository.deleteByCodeAndDate(code, date);
    }

    public void printExchangeRates() {
        ConsolePrinter.printExchangeRates(
                Arrays.asList(
                        this.findFirstByCodeOrderByDateDesc("EUR"),
                        this.findFirstByCodeOrderByDateDesc("USD")
                )
        );
    }

}


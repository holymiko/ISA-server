package home.holymiko.investment.scraper.app.server.service;

import home.holymiko.investment.scraper.app.server.type.entity.ExchangeRate;
import home.holymiko.investment.scraper.app.server.api.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;


@Service
@AllArgsConstructor
public class RateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public Long count() {
        return this.exchangeRateRepository.count();
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


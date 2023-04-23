package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

@SpringBootTest
class CurrencyServiceIntegrationTest {

    @Autowired
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        ExchangeRate exchangeRate0 = new ExchangeRate(new Date(200000000000L), "country", "currency", 1, "code", 25.0);
        ExchangeRate exchangeRate1 = new ExchangeRate(new Date(500000000001L), "country1", "currency1", 1, "code1", 15.1);
        ExchangeRate exchangeRate2 = new ExchangeRate(new Date(800000000002L), "country2", "currency2", 1, "code2", 15.2);
        ExchangeRate exchangeRate2Copy = new ExchangeRate(new Date(700000000002L), "country2", "currency2", 1, "code2", 155.2);

        ExchangeRate saved0 = currencyService.save(exchangeRate0);
        ExchangeRate saved1 = currencyService.save(exchangeRate1);
        ExchangeRate saved2 = currencyService.save(exchangeRate2);
        ExchangeRate saved2Copy = currencyService.save(exchangeRate2Copy);

        exchangeRateEquals(saved0, exchangeRate0);
        exchangeRateEquals(saved1, exchangeRate1);
        exchangeRateEquals(saved2, exchangeRate2);
        exchangeRateEquals(saved2Copy, exchangeRate2Copy);

        ExchangeRate found0 = currencyService.findExchangeRate("code");
        ExchangeRate found1 = currencyService.findExchangeRate("code1");
        ExchangeRate found2 = currencyService.findExchangeRate("code2");

        exchangeRateEquals(found0, exchangeRate0);
        exchangeRateEquals(found1, exchangeRate1);
        exchangeRateEquals(found2, exchangeRate2);

        // Overwrite ExchangeRate because of same Date and code
        ExchangeRate exchangeRateNew0 = new ExchangeRate(new Date(200000000000L), "country", "currency", 1, "code", 458.0);
        currencyService.save(exchangeRateNew0);
        ExchangeRate foundNew0 = currencyService.findExchangeRate("code");
        // Overwrite success
        exchangeRateEquals(foundNew0, exchangeRateNew0);
        currencyService.delete(exchangeRateNew0.getCode(), exchangeRateNew0.getDate());
        // Previous was removed during saved
        Assertions.assertNull( currencyService.findExchangeRate("code") );

        // Newer is removed, older is found. Older is removed, non is found
        ExchangeRate ex = currencyService.findExchangeRate("code2");
        exchangeRateEquals(exchangeRate2, ex);
        currencyService.delete(exchangeRate2.getCode(), exchangeRate2.getDate());
        ExchangeRate ex2 = currencyService.findExchangeRate("code2");
        exchangeRateEquals(exchangeRate2Copy, ex2);
        currencyService.delete(exchangeRate2Copy.getCode(), exchangeRate2Copy.getDate());
        Assertions.assertNull( currencyService.findExchangeRate("code2") );

        // Clean
        currencyService.delete( exchangeRate1.getCode(), exchangeRate1.getDate() );
        Assertions.assertNull( currencyService.findExchangeRate("code") );
        Assertions.assertNull( currencyService.findExchangeRate("code1") );
        Assertions.assertNull( currencyService.findExchangeRate("code2") );
    }

    private static void exchangeRateEquals(ExchangeRate saved, ExchangeRate origin) {
        Assertions.assertEquals(saved.getDate().toString(), origin.getDate().toString());
        Assertions.assertEquals(saved.getCountry(), origin.getCountry());
        Assertions.assertEquals(saved.getCurrency(), origin.getCurrency());
        Assertions.assertEquals(saved.getAmount(), origin.getAmount());
        Assertions.assertEquals(saved.getCode(), origin.getCode());
        // compare doubles with a delta
        Assertions.assertEquals(saved.getExchangeRate(), origin.getExchangeRate(), 0.001);
    }

    @Test
    void findExchangeRate() {
    }

    @Test
    void delete() {
    }
}
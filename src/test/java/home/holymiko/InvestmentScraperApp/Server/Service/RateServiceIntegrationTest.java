package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Run;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;

@Disabled("TODO Activate")
@SpringBootTest
class RateServiceIntegrationTest {

    // Prevent @EventListener
    @MockBean
    private Run run;
    @MockBean
    private MetalScraper metalScraper;

    @Autowired
    private RateService rateService;

    @Test
    void exchangeRateIntegrationTest() {
        ExchangeRate exchangeRate0 = new ExchangeRate(new Date(200000000000L), "country", "currency", 1, "code", 25.0);
        ExchangeRate exchangeRate1 = new ExchangeRate(new Date(500000000001L), "country1", "currency1", 1, "code1", 15.1);
        ExchangeRate exchangeRate2 = new ExchangeRate(new Date(800000000002L), "country2", "currency2", 1, "code2", 15.2);
        ExchangeRate exchangeRate2Copy = new ExchangeRate(new Date(700000000002L), "country2", "currency2", 1, "code2", 155.2);

        ExchangeRate saved0 = rateService.save(exchangeRate0);
        ExchangeRate saved1 = rateService.save(exchangeRate1);
        ExchangeRate saved2 = rateService.save(exchangeRate2);
        ExchangeRate saved2Copy = rateService.save(exchangeRate2Copy);

        exchangeRateEquals(saved0, exchangeRate0);
        exchangeRateEquals(saved1, exchangeRate1);
        exchangeRateEquals(saved2, exchangeRate2);
        exchangeRateEquals(saved2Copy, exchangeRate2Copy);

        ExchangeRate found0 = rateService.findExchangeRate("code");
        ExchangeRate found1 = rateService.findExchangeRate("code1");
        ExchangeRate found2 = rateService.findExchangeRate("code2");

        exchangeRateEquals(found0, exchangeRate0);
        exchangeRateEquals(found1, exchangeRate1);
        exchangeRateEquals(found2, exchangeRate2);

        // Overwrite ExchangeRate because of same Date and code
        ExchangeRate exchangeRateNew0 = new ExchangeRate(new Date(200000000000L), "country", "currency", 1, "code", 458.0);
        rateService.save(exchangeRateNew0);
        ExchangeRate foundNew0 = rateService.findExchangeRate("code");
        // Overwrite success
        exchangeRateEquals(foundNew0, exchangeRateNew0);
        rateService.delete(exchangeRateNew0.getCode(), exchangeRateNew0.getDate());
        // Previous was removed during saved
        Assertions.assertNull( rateService.findExchangeRate("code") );

        // Newer is removed, older is found. Older is removed, non is found
        ExchangeRate ex = rateService.findExchangeRate("code2");
        exchangeRateEquals(exchangeRate2, ex);
        rateService.delete(exchangeRate2.getCode(), exchangeRate2.getDate());
        ExchangeRate ex2 = rateService.findExchangeRate("code2");
        exchangeRateEquals(exchangeRate2Copy, ex2);
        rateService.delete(exchangeRate2Copy.getCode(), exchangeRate2Copy.getDate());
        Assertions.assertNull( rateService.findExchangeRate("code2") );

        // Clean
        rateService.delete( exchangeRate1.getCode(), exchangeRate1.getDate() );
        Assertions.assertNull( rateService.findExchangeRate("code") );
        Assertions.assertNull( rateService.findExchangeRate("code1") );
        Assertions.assertNull( rateService.findExchangeRate("code2") );
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

}
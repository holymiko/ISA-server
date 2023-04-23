package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Run;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.time.LocalDateTime;

@SpringBootTest
class ProductServiceIntegrationTest {

    // Prevent @EventListener
    @MockBean
    private Run run;

    @Autowired
    private ProductService productService;

    @Test
    void currencyIntegrationTest() {

        Product savedP0 = productService.save( new Product("test0", Producer.ARGOR_HERAEUS, Form.BAR, Metal.GOLD, 1, 2015, false) );
        Product savedP1 = productService.save( new Product("test1", Producer.HERAEUS, Form.KINEBAR, Metal.SILVER, 1, 2015, false) );

        Assertions.assertEquals(2, productService.findAllAsDTO().size() );

        // Clean
        productService.deleteById(savedP0.getId());
        productService.deleteById(savedP1.getId());
        Assertions.assertEquals(0, productService.findAllAsDTO().size() );
    }



}
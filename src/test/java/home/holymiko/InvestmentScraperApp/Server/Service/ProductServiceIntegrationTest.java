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
    @MockBean
    private MetalScraper metalScraper;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ProductService productService;

    @Test
    void currencyIntegrationTest() {

        Product savedP0 = productService.save( new Product("test0", Producer.ARGOR_HERAEUS, Form.BAR, Metal.GOLD, 1, 2015, false) );
        Product savedP1 = productService.save( new Product("test1", Producer.HERAEUS, Form.KINEBAR, Metal.SILVER, 1, 2015, false) );

//        TODO Activate and check cascade delete
//        Price price0 = priceService.save( new Price(LocalDateTime.of(2022, 5, 5, 2, 2), 5.0, false) );
//        Price price1 = priceService.save( new Price(LocalDateTime.of(2022, 5, 5, 2, 2), 4.0, true) );
//        Price price2 = priceService.save( new Price(LocalDateTime.of(2023, 2, 5, 2, 2), 550.0, false) );
//        Price price3 = priceService.save( new Price(LocalDateTime.of(2023, 2, 5, 2, 2), 450.0, true) );
//        Price price4 = priceService.save( new Price(LocalDateTime.now(), 55.0, false) );
//        Price price5 = priceService.save( new Price(LocalDateTime.now(), 45.0, true) );
//
//        PricePair pricePair0 = new PricePair( Dealer.ZLATAKY, price0, price1, savedP0.getId() );
//        PricePair pricePair1 = new PricePair( Dealer.ZLATAKY, price2, price3, savedP0.getId() );
//        PricePair pricePair2 = new PricePair( Dealer.ZLATAKY, price4, price5, savedP0.getId() );
//
//        PricePair savedPP0 = priceService.save(pricePair0);
//        PricePair savedPP1 = priceService.save(pricePair1);
//        PricePair savedPP2 = priceService.save(pricePair2);
//
//        productService.updatePrices(savedP0.getId(), savedPP0);
//        productService.updatePrices(savedP0.getId(), savedPP1);
//        productService.updatePrices(savedP0.getId(), savedPP2);

        Assertions.assertEquals(2, productService.findAllAsDTO().size() );
//        Assertions.assertEquals(3, productService.findByIdAsDTOAllPrices(savedP0.getId()).getPrices().size());


        // Clean
        productService.deleteById(savedP0.getId());
        productService.deleteById(savedP1.getId());
        Assertions.assertEquals(0, productService.findAllAsDTO().size() );
        // Delete is cascade
        Assertions.assertEquals(0, priceService.count() );
    }



}
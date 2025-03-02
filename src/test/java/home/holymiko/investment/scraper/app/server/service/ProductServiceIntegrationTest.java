package home.holymiko.investment.scraper.app.server.service;

import home.holymiko.investment.scraper.app.Run;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.MetalScraper;
import home.holymiko.investment.scraper.app.server.type.dto.create.ProductCreateDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Product;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.type.enums.Producer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

@Disabled("TODO Activate")
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
    void productIntegrationTest() {

        Product savedP0 = productService.save( new Product(new ProductCreateDTO("test0", Producer.ARGOR_HERAEUS, Form.BAR, Metal.GOLD, 1.0, 2015, false, false)));
        Product savedP1 = productService.save( new Product(new ProductCreateDTO("test1", Producer.HERAEUS, Form.KINEBAR, Metal.SILVER, 1.0, 2015, false, false)));

        Assertions.assertEquals(2, productService.findByParams(null, null, null, null, null, null, null, null, Pageable.unpaged()).getTotalElements());

        // Clean
        productService.deleteById(savedP0.getId());
        productService.deleteById(savedP1.getId());
        Assertions.assertEquals(0, productService.findByParams(null, null, null, null, null, null, null, null, Pageable.unpaged()).getTotalElements());
    }
}
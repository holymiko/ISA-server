package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ProductControllerTest {


    @Mock
    private ProductService productService;

    private ProductController productController;
    private MockMvc mockMvc;

    @BeforeAll
    private void before() {
        Mockito.reset();
        this.productController = new ProductController(productService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void byId() {
        when(productService.findByIdAsDTOAllPrices(anyLong())).then(null);
//        this.mockMvc.perform("/api/v2/product/id/{id}", 5)
    }
}
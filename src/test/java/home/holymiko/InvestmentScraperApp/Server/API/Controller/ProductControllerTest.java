package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @MockBean
    private ProductService productService;
    private ProductController productController;
    private MockMvc mockMvc;

    @BeforeAll
    public void before() {
        Mockito.reset();
        this.productController = new ProductController(productService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void byId200() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenReturn(new ProductDTO_AllPrices(1, "", Metal.GOLD, Form.BAR, 2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        this.mockMvc.perform(get("/api/v2/product/id/{id}", 5))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void byId404() throws Exception {
        String myMsg = "Some error msg";
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException(myMsg));
        this.mockMvc.perform(get("/api/v2/product/id/{id}", 5))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals(myMsg, result.getResolvedException().getMessage()));
    }
}
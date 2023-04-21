package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @MockBean
    private ProductService productService;
    private ProductController productController;
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        Mockito.reset();
        this.productController = new ProductController(productService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void byId200() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenReturn(new ProductDTO_AllPrices(1, "", Metal.GOLD, Form.BAR, 2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        this.mockMvc.perform(get("/api/v2/product/{id}", 5))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productService, times(1)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byId404() throws Exception {
        String myMsg = "Some error msg";
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException(myMsg));
        this.mockMvc.perform(get("/api/v2/product/{id}", 5))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals(myMsg, result.getResolvedException().getMessage()));
        verify(productService, times(1)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byId404NullId() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/{id}", (Object) null))
                .andExpect(status().isNotFound());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byId400RandomString() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/{id}", "sdf"))
                .andExpect(status().isBadRequest());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byIdAsDto200() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenReturn(new ProductDTO_AllPrices(1, "", Metal.GOLD, Form.BAR, 2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        this.mockMvc.perform(get("/api/v2/product/{id}", 5))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(productService, times(1)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byIdAsDto404() throws Exception {
        String myMsg = "Some error msg";
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException(myMsg));
        this.mockMvc.perform(get("/api/v2/product/{id}", 5))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals(myMsg, result.getResolvedException().getMessage()));
        verify(productService, times(1)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byIdAsDto404NullId() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/{id}", (Object) null))
                .andExpect(status().isNotFound());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byIdAsDto404NullId2() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/"))
                .andExpect(status().isNotFound());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byIdAsDto400RandomString() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/{id}", "sdf"))
                .andExpect(status().isBadRequest());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byMetalEmpty() throws Exception {
        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "SILVER"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "[]"));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalEmptyLowerCase() throws Exception {
        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "silver"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "[]"));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalFull() throws Exception {
        ProductDTO_LatestPrices p1 = new ProductDTO_LatestPrices(1, "p1", Metal.GOLD, Form.BAR, 20, new ArrayList<>(), new ArrayList<>());
        ProductDTO_LatestPrices p2 = new ProductDTO_LatestPrices(2, "P2", Metal.GOLD, Form.COIN, 30, new ArrayList<>(), new ArrayList<>());

        when(productService.findByMetalAsDTO(any())).thenReturn(Arrays.asList(p1, p2));
        this.mockMvc.perform(get("/api/v2/product/metal/{metal}", "gold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("p1"))
                .andExpect(jsonPath("$[0].metal").value("GOLD"))
                .andExpect(jsonPath("$[0].form").value("BAR"))
                .andExpect(jsonPath("$[0].grams").value(20))
//                .andExpect(jsonPath("$[0].links").value("[]"))
//                .andExpect(jsonPath("$[0].latestPrices").value("[]"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("P2"))
                .andExpect(jsonPath("$[1].metal").value("GOLD"))
                .andExpect(jsonPath("$[1].form").value("COIN"));
//                .andExpect(jsonPath("$[1].links").value("[]"))
//                .andExpect(jsonPath("$[1].latestPrices").value("[]"));
        verify(productService, times(1)).findByMetalAsDTO(any());
    }

    @Test
    void byMetal404NullParam() throws Exception {
        when(productService.findByMetalAsDTO(any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product/metal/{metal}", (Object) null))
                .andExpect(status().isNotFound());
        verify(productService, times(0)).findByMetalAsDTO(any());
    }

    @Test
    void byMetal404NullParam2() throws Exception {
        when(productService.findByMetalAsDTO(any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product/metal/"))
                .andExpect(status().isNotFound());
        verify(productService, times(0)).findByMetalAsDTO(any());
    }

    @Test
    void byMetal400RandomString() throws Exception {
        when(productService.findByMetalAsDTO(any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product/metal/{metal}", "sdf"))
                .andExpect(status().isBadRequest());
//                .andExpect(result -> assertEquals("Only possible values for path variable are: 'gold', 'silver', 'platinum' and 'palladium'", result.getResolvedException().getMessage()));
        verify(productService, times(0)).findByMetalAsDTO(any());
    }

    @Test
    void byMetalUpperCaseParam() throws Exception {
        when(productService.findByMetalAsDTO(any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product/metal/{metal}", "PLATINUM"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "[]"));
        verify(productService, times(1)).findByMetalAsDTO(any());
    }

    @Test
    void byMetalOtherPath() throws Exception {
        when(productService.findByMetalAsDTO(any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product/metal/{metal}/", "palladium"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "[]"));
        verify(productService, times(1)).findByMetalAsDTO(any());
    }

    @Test
    void allAsDtoEmpty() throws Exception {
        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "[]"));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void allAsDtoFull() throws Exception {
        ProductDTO_LatestPrices p1 = new ProductDTO_LatestPrices(1, "p1", Metal.GOLD, Form.BAR, 20, new ArrayList<>(), new ArrayList<>());
        ProductDTO_LatestPrices p2 = new ProductDTO_LatestPrices(2, "P2", Metal.GOLD, Form.COIN, 30, new ArrayList<>(), new ArrayList<>());

        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any())).thenReturn(Arrays.asList(p1, p2));
        this.mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("p1"))
                .andExpect(jsonPath("$[0].metal").value("GOLD"))
                .andExpect(jsonPath("$[0].form").value("BAR"))
                .andExpect(jsonPath("$[0].grams").value(20))
//                .andExpect(jsonPath("$[0].links").value("[]"))
//                .andExpect(jsonPath("$[0].latestPrices").value("[]"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("P2"))
                .andExpect(jsonPath("$[1].metal").value("GOLD"))
                .andExpect(jsonPath("$[1].form").value("COIN"));
//                .andExpect(jsonPath("$[1].links").value("[]"))
//                .andExpect(jsonPath("$[1].latestPrices").value("[]"));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any());
    }
}
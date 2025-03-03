package home.holymiko.investment.scraper.app.server.api.controller;

import home.holymiko.investment.scraper.app.server.core.config.SpringConfig;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.service.ProductService;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_AllPrices;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_LatestPrices;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SpringConfig.class)
@ContextConfiguration(classes = ProductController.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private ProductService productService;
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        Mockito.reset();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void byId200() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenReturn(new ProductDTO_AllPrices(1, "", Metal.GOLD, Form.BAR, 2, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
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
    void byId400RandomString() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/{id}", "sdf"))
                .andExpect(status().isBadRequest());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
    }

    @Test
    void byIdAsDto200() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenReturn(new ProductDTO_AllPrices(1, "", Metal.GOLD, Form.BAR, 2, false, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
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

    @Disabled
    @Test
    void byIdNullRedirect() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/{id}", (Object) null))
                .andExpect(status().isOk());
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
        verify(productService, times(0)).findByIdAsDTOLinkAllPrices(any());
    }

    @Disabled
    @Test
    void byIdNullRedirect2() throws Exception {
        when(productService.findByIdAsDTOAllPrices(any())).thenThrow(new ResourceNotFoundException());
        this.mockMvc.perform(get("/api/v2/product/"))
                .andExpect(status().isOk());
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(productService, times(0)).findByIdAsDTOAllPrices(any());
        verify(productService, times(0)).findByIdAsDTOLinkAllPrices(any());
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
        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Page.empty());
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "SILVER"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().startsWith("{\"content\":[],")));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalEmptyLowerCase() throws Exception {
        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Page.empty());
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "silver"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().startsWith("{\"content\":[],")));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalFull() throws Exception {
        ProductDTO_LatestPrices p1 = new ProductDTO_LatestPrices(1, "p1", Metal.GOLD, Form.BAR, 20, false, new ArrayList<>(), new ArrayList<>());
        ProductDTO_LatestPrices p2 = new ProductDTO_LatestPrices(2, "P2", Metal.GOLD, Form.COIN, 30, false, new ArrayList<>(), new ArrayList<>());

        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(Arrays.asList(p1, p2), PageRequest.of(0, 5), 2));
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "GOLD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.[0].id").value(1))
                .andExpect(jsonPath("content.[0].name").value("p1"))
                .andExpect(jsonPath("content.[0].metal").value("GOLD"))
                .andExpect(jsonPath("content.[0].form").value("BAR"))
                .andExpect(jsonPath("content.[0].grams").value(20))
//                .andExpect(jsonPath("content.[0].links").value("[]"))
//                .andExpect(jsonPath("content.[0].latestPrices").value("[]"))
                .andExpect(jsonPath("content.[1].id").value(2))
                .andExpect(jsonPath("content.[1].name").value("P2"))
                .andExpect(jsonPath("content.[1].metal").value("GOLD"))
                .andExpect(jsonPath("content.[1].form").value("COIN"));
//                .andExpect(jsonPath("content.[1].links").value("[]"))
//                .andExpect(jsonPath("content.[1].latestPrices").value("[]"));
        verify(productService, times(1)).findByParams(any(), any(), eq(Metal.GOLD), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalNullRedirect() throws Exception {
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", (String) null))
                .andExpect(status().isOk());
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Disabled
    @Test
    void byMetalRedirect400() throws Exception {
        this.mockMvc.perform(get("/api/v2/product/metal/"))
                .andExpect(status().isBadRequest());
        verify(productService, times(0)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalUpperCaseParam() throws Exception {
        when(productService.findByParams(any(), any(), eq(Metal.PLATINUM), any(), any(), any(), any(), any(), any())).thenReturn(Page.empty());
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "PLATINUM"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().startsWith("{\"content\":[],")));
        verify(productService, times(1)).findByParams(any(), any(), eq(Metal.PLATINUM), any(), any(), any(), any(), any(), any());
    }

    @Test
    void byMetalLowerCase() throws Exception {
        this.mockMvc.perform(get("/api/v2/product")
                        .param("metal", "palladium"))
                .andExpect(status().isOk());
        verify(productService, times(1)).findByParams(any(), any(), eq(Metal.PALLADIUM), any(), any(), any(), any(), any(), any());
    }

    @Test
    void allAsDtoEmpty() throws Exception {
        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Page.empty());
        this.mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().startsWith("{\"content\":[],")));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void allAsDtoFull() throws Exception {
        ProductDTO_LatestPrices p1 = new ProductDTO_LatestPrices(1, "p1", Metal.GOLD, Form.BAR, 20, false, new ArrayList<>(), new ArrayList<>());
        ProductDTO_LatestPrices p2 = new ProductDTO_LatestPrices(2, "P2", Metal.GOLD, Form.COIN, 30, false, new ArrayList<>(), new ArrayList<>());

        when(productService.findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(Arrays.asList(p1, p2), PageRequest.of(0, 5), 2));
        this.mockMvc.perform(get("/api/v2/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content.[0].id").value(1))
                .andExpect(jsonPath("content.[0].name").value("p1"))
                .andExpect(jsonPath("content.[0].metal").value("GOLD"))
                .andExpect(jsonPath("content.[0].form").value("BAR"))
                .andExpect(jsonPath("content.[0].grams").value(20))
//                .andExpect(jsonPath("content.[0].links").value("[]"))
//                .andExpect(jsonPath("content.[0].latestPrices").value("[]"))
                .andExpect(jsonPath("content.[1].id").value(2))
                .andExpect(jsonPath("content.[1].name").value("P2"))
                .andExpect(jsonPath("content.[1].metal").value("GOLD"))
                .andExpect(jsonPath("content.[1].form").value("COIN"));
//                .andExpect(jsonPath("content.[1].links").value("[]"))
//                .andExpect(jsonPath("content.[1].latestPrices").value("[]"));
        verify(productService, times(1)).findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}
package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.config.SpringConfig;
import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SpringConfig.class)
@ContextConfiguration(classes = LinkController.class)
@WebMvcTest(LinkController.class)
class LinkControllerTest {
    @Autowired
    private WebApplicationContext context;
    @MockBean
    private LinkService linkService;
    @MockBean
    private ProductService productService;
    private MockMvc mockMvc;

    @BeforeEach
    public void before() {
        Mockito.reset();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void domain200() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link"))
                .andExpect(status().isOk());
        verify(linkService, times(1)).findByParams(any(), any());
    }

    @Test
    void findByParams200() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link")
                        .param("dealer", String.valueOf(Dealer.ZLATAKY)))
                .andExpect(status().isOk());
        verify(linkService, times(1)).findByParams(any(), any());
    }

    @Test
    void findByParamsRandomCase() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link")
                        .param("dealer", "zLaTakY"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(linkService, times(1)).findByParams(any(), any());
    }

    @Test
    void findByParamsLowerCase() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link")
                        .param("dealer", "zlataky"))
                .andExpect(status().isOk());
        verify(linkService, times(1)).findByParams(any(), any());
    }

    @Test
    void findByParamsUpperCase() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link")
                        .param("dealer", "ZLATAKY"))
                .andExpect(status().isOk());
        verify(linkService, times(1)).findByParams(any(), any());
    }

    @Test
    void findByParams400RandomString() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link")
                        .param("dealer", "sdf"))
                .andExpect(status().isBadRequest());
        verify(linkService, times(0)).findByParams(any(), any());
    }

    @Test
    void findByParamsIgnoreEmptyString() throws Exception {
        when(linkService.findByParams(any(), any())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/api/v2/link")
                        .param("dealer", ""))
                .andExpect(status().isOk());
        verify(linkService, times(1)).findByParams(any(), any());
    }

}
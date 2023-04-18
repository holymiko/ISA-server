package home.holymiko.InvestmentScraperApp.Server.API.TextPort;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

class ExportTest {

    private final Export export = new Export();

    @Test
    void exportObjectToXmlAndJson() throws IOException, JAXBException {
        // Mock
        Link link00 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/1-g-argor-heraeus-sa-svycarsko-investicni-zlaty-slitek", "Test");
        Link link01 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/1810/s/zlaty-slitek-1-g-argor-heraeus-svycarsko/category/52/", "Test");
        Link link10 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/stribrna-investicni-mince-wiener-philharmoniker-1-oz", "Test");
        Link link11 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/1834/s/wiener-philharmoniker-1-trojska-unce-stribrna-mince-rakousko/category/84/", "Test");
        Link link20 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/5000g-heraeus-nemecko-investicni-stribrny-slitek", "Test");
        Link link30 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/2227/s/platinovy-slitek-5-g-argor-heraeus-svycarsko/category/96/", "Test");

        PricePair pricePair00 = new PricePair(
                new Price(LocalDateTime.now(), 1808.45, false),
                new Price(LocalDateTime.now(), 1428.50, true),
                -1L
        );
        PricePair pricePair01 = new PricePair(
                new Price(LocalDateTime.now(), 1818.45, false),
                new Price(LocalDateTime.now(), 1528.50, true),
                -1L
        );
        PricePair pricePair10 = new PricePair(
                new Price(LocalDateTime.now().minusHours(2), 718.45, false),
                new Price(LocalDateTime.now().minusHours(2), 428.50, true),
                -1L
        );
        PricePair pricePair11 = new PricePair(
                new Price(LocalDateTime.now().minusHours(2), 918.45, false),
                new Price(LocalDateTime.now().minusHours(2), 628.50, true),
                -1L
        );
        PricePair pricePair20 = new PricePair(
                new Price(LocalDateTime.now().minusDays(1), 108958.00, false),
                new Price(LocalDateTime.now().minusDays(1), 100000.98, true),
                -1L
        );
        PricePair pricePair30 = new PricePair(
                new Price(LocalDateTime.now().minusDays(1), 6014.78, false),
                new Price(LocalDateTime.now().minusDays(1), 5014.78, true),
                -1L
        );

        link00.setPricePairs(
                List.of(pricePair00)
        );
        link01.setPricePairs(
                List.of(pricePair01)
        );
        link11.setPricePairs(
                List.of(pricePair11)
        );
        link10.setPricePairs(
                List.of(pricePair10)
        );
        link20.setPricePairs(
                List.of(pricePair20)
        );
        link30.setPricePairs(
                List.of(pricePair30)
        );

        Product product0 = new Product("Zlatý slitek 1g ARGOR-HERAEUS (Švýcarsko)", Producer.ARGOR_HERAEUS, Form.BAR, Metal.GOLD, 1.0, 2023, false, Arrays.asList(link00, link01));
        Product product1 = new Product("Stříbrná mince 1 oz (trojská unce) WIENER PHILHARMONIKER Rakousko 2011", Producer.MUNZE_OSTERREICH, Form.COIN, Metal.SILVER, 31.1, 2022, false, Arrays.asList(link10, link11));
        Product product2 = new Product("5000g Argor Heraeus / Heraeus Investiční stříbrný slitek", Producer.MUNZE_OSTERREICH, Form.BAR, Metal.SILVER, 5000, 2023, false, List.of(link20));
        Product product3 = new Product("Platinový slitek 5 g ARGOR-HERAEUS (Švýcarsko)", Producer.HERAEUS, Form.BAR, Metal.PLATINUM, 5, 2021, false, List.of(link30));

        InvestmentMetal investmentMetal0 = new InvestmentMetal(product0, Dealer.ZLATAKY, 1808.45, LocalDate.now());
        InvestmentMetal investmentMetal1 = new InvestmentMetal(product1, Dealer.BESSERGOLD_CZ, 858.00, LocalDate.now().minusWeeks(65));
        InvestmentMetal investmentMetal2 = new InvestmentMetal(product2, Dealer.ZLATAKY, 10205.12, LocalDate.now().minusDays(3));
        InvestmentMetal investmentMetal3 = new InvestmentMetal(product3, Dealer.BESSERGOLD_CZ, 5865.00, LocalDate.now().minusMonths(1));

        StockGraham stock0 = new StockGraham(null, "stock0", null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
        StockGraham stock1 = new StockGraham(null, "stock1", null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);

        InvestmentStock investmentStock0 = new InvestmentStock(stock0, 5, 100, LocalDate.now());
        InvestmentStock investmentStock1 = new InvestmentStock(stock1, 3, 845, LocalDate.now().minusWeeks(2));

        Portfolio portfolio = new Portfolio(
                Arrays.asList(investmentMetal0, investmentMetal1, investmentMetal2, investmentMetal3),
                Arrays.asList(investmentStock0, investmentStock1)
        );

        export.exportToJSON(portfolio);
        export.exportToJSON(product0);
        export.exportToJSON(stock0);
        export.exportToJSON(investmentMetal0);
        export.exportToJSON(investmentStock0);
        export.exportToJSON(pricePair00);
        export.exportToXML(portfolio);
        export.exportToXML(product0);
        export.exportToXML(stock0);
        export.exportToXML(investmentMetal0);
        export.exportToXML(investmentStock0);
        export.exportToXML(pricePair00);
    }

}
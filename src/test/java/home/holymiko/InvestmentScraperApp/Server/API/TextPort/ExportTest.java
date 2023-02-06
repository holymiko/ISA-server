package home.holymiko.InvestmentScraperApp.Server.API.TextPort;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

class ExportTest {

    private final Export export = new Export();

    @Test
    void exportProductToPDF() {
        // Mock
        Link link0 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/1-g-argor-heraeus-sa-svycarsko-investicni-zlaty-slitek");
        Link link1 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/1810/s/zlaty-slitek-1-g-argor-heraeus-svycarsko/category/52/");
        PricePair pricePair00 = new PricePair(
                Dealer.ZLATAKY,
                new Price(LocalDateTime.now(), 1808.45, false),
                new Price(LocalDateTime.now(), 1428.50, true),
                -1L
        );
        PricePair pricePair01 = new PricePair(
                Dealer.ZLATAKY,
                new Price(LocalDateTime.now().minusHours(2), 1718.45, false),
                new Price(LocalDateTime.now().minusHours(2), 1428.50, true),
                -1L
        );
        PricePair pricePair02 = new PricePair(
                Dealer.ZLATAKY,
                new Price(LocalDateTime.now().minusDays(1), 1818.45, false),
                new Price(LocalDateTime.now().minusDays(1), 1528.50, true),
                -1L
        );
        PricePair pricePair10 = new PricePair(
                Dealer.BESSERGOLD_CZ,
                new Price(LocalDateTime.now(), 1818.45, false),
                new Price(LocalDateTime.now(), 1528.50, true),
                -1L
        );
        PricePair pricePair11 = new PricePair(
                Dealer.BESSERGOLD_CZ,
                new Price(LocalDateTime.now().minusHours(2), 1918.45, false),
                new Price(LocalDateTime.now().minusHours(2), 1628.50, true),
                -1L
        );
        PricePair pricePair12 = new PricePair(
                Dealer.BESSERGOLD_CZ,
                new Price(LocalDateTime.now().minusDays(1), 1718.45, false),
                new Price(LocalDateTime.now().minusDays(1), 1428.50, true),
                -1L
        );

        Product product = new Product("Zlatý slitek 1g ARGOR-HERAEUS (Švýcarsko)", Producer.ARGOR_HERAEUS, Form.BAR, Metal.GOLD, 1.0, 2023, false);
        product.setLinks(
                Arrays.asList(link0, link1)
        );
        product.setPricePairs(
                Arrays.asList(pricePair00, pricePair10, pricePair01, pricePair11, pricePair02, pricePair12)
        );

        export.exportToPDF(product);
    }

    @Test
    void exportPortfolioToPDF() {
        // Mock
        Link link00 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/1-g-argor-heraeus-sa-svycarsko-investicni-zlaty-slitek");
        Link link01 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/1810/s/zlaty-slitek-1-g-argor-heraeus-svycarsko/category/52/");
        Link link10 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/stribrna-investicni-mince-wiener-philharmoniker-1-oz");
        Link link11 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/1834/s/wiener-philharmoniker-1-trojska-unce-stribrna-mince-rakousko/category/84/");
        Link link20 = new Link(Dealer.ZLATAKY, "https://zlataky.cz/5000g-heraeus-nemecko-investicni-stribrny-slitek");
        Link link30 = new Link(Dealer.BESSERGOLD_CZ, "https://www.bessergold.cz/cs/catalog/product/view/id/2227/s/platinovy-slitek-5-g-argor-heraeus-svycarsko/category/96/");

        PricePair pricePair00 = new PricePair(
                Dealer.ZLATAKY,
                new Price(LocalDateTime.now(), 1808.45, false),
                new Price(LocalDateTime.now(), 1428.50, true),
                -1L
        );
        PricePair pricePair01 = new PricePair(
                Dealer.BESSERGOLD_CZ,
                new Price(LocalDateTime.now(), 1818.45, false),
                new Price(LocalDateTime.now(), 1528.50, true),
                -1L
        );
        PricePair pricePair10 = new PricePair(
                Dealer.ZLATAKY,
                new Price(LocalDateTime.now().minusHours(2), 718.45, false),
                new Price(LocalDateTime.now().minusHours(2), 428.50, true),
                -1L
        );
        PricePair pricePair11 = new PricePair(
                Dealer.BESSERGOLD_CZ,
                new Price(LocalDateTime.now().minusHours(2), 918.45, false),
                new Price(LocalDateTime.now().minusHours(2), 628.50, true),
                -1L
        );
        PricePair pricePair20 = new PricePair(
                Dealer.ZLATAKY,
                new Price(LocalDateTime.now().minusDays(1), 108958.00, false),
                new Price(LocalDateTime.now().minusDays(1), 100000.98, true),
                -1L
        );
        PricePair pricePair30 = new PricePair(
                Dealer.BESSERGOLD_CZ,
                new Price(LocalDateTime.now().minusDays(1), 6014.78, false),
                new Price(LocalDateTime.now().minusDays(1), 5014.78, true),
                -1L
        );

        Product product0 = new Product("Zlatý slitek 1g ARGOR-HERAEUS (Švýcarsko)", Producer.ARGOR_HERAEUS, Form.BAR, Metal.GOLD, 1.0, 2023, false);
        Product product1 = new Product("Stříbrná mince 1 oz (trojská unce) WIENER PHILHARMONIKER Rakousko 2011", Producer.MUNZE_OSTERREICH, Form.COIN, Metal.SILVER, 31.1, 2022, false);
        Product product2 = new Product("5000g Argor Heraeus / Heraeus Investiční stříbrný slitek", Producer.MUNZE_OSTERREICH, Form.BAR, Metal.SILVER, 5000, 2023, false);
        Product product3 = new Product("Platinový slitek 5 g ARGOR-HERAEUS (Švýcarsko)", Producer.HERAEUS, Form.BAR, Metal.PLATINUM, 5, 2021, false);

        product0.setLinks(
                Arrays.asList(link00, link01)
        );
        product0.setPricePairs(
                Arrays.asList(pricePair00, pricePair01)
        );
        product1.setLinks(
                Arrays.asList(link10, link11)
        );
        product1.setPricePairs(
                Arrays.asList(pricePair10, pricePair11)
        );
        product2.setLinks(
                Arrays.asList(link20)
        );
        product2.setPricePairs(
                Arrays.asList(pricePair20)
        );
        product3.setLinks(
                Arrays.asList(link30)
        );
        product3.setPricePairs(
                Arrays.asList(pricePair30)
        );

        InvestmentMetal investmentMetal0 = new InvestmentMetal(product0, Dealer.ZLATAKY, 1808.45, LocalDate.now());
        InvestmentMetal investmentMetal1 = new InvestmentMetal(product1, Dealer.BESSERGOLD_CZ, 858.00, LocalDate.now().minusWeeks(65));
        InvestmentMetal investmentMetal2 = new InvestmentMetal(product2, Dealer.ZLATAKY, 102058.12, LocalDate.now().minusDays(3));
        InvestmentMetal investmentMetal3 = new InvestmentMetal(product3, Dealer.BESSERGOLD_CZ, 5865.00, LocalDate.now().minusMonths(1));

        GrahamStock stock0 = new GrahamStock();
        GrahamStock stock1 = new GrahamStock();

        InvestmentStock investmentStock0 = new InvestmentStock(stock0, 5, 45, LocalDate.now());
        InvestmentStock investmentStock1 = new InvestmentStock(stock1, 10589, 845, LocalDate.now().minusWeeks(2));

        Portfolio portfolio = new Portfolio(
                "Nelson Mandela",
                Arrays.asList(investmentMetal0, investmentMetal1, investmentMetal2, investmentMetal3),
                Arrays.asList(investmentStock0, investmentStock1)
        );

        export.exportToPDF(portfolio);
    }

}
package home.holymiko.investment.scraper.app.server.utils;

import home.holymiko.investment.scraper.app.server.api.repository.ProductRepository;
import home.holymiko.investment.scraper.app.server.api.repository.StockGrahamRepository;
import home.holymiko.investment.scraper.app.server.type.entity.Product;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.type.enums.Producer;
import home.holymiko.investment.scraper.app.server.service.InvestmentService;
import home.holymiko.investment.scraper.app.server.service.PortfolioService;
import home.holymiko.investment.scraper.app.server.type.entity.InvestmentMetal;
import home.holymiko.investment.scraper.app.server.type.entity.InvestmentStock;
import home.holymiko.investment.scraper.app.server.type.entity.Portfolio;
import home.holymiko.investment.scraper.app.server.type.entity.StockGraham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@Component
public class InvestmentInit {

    private static final double TROY_OUNCE = 31.1034768;

    private final StockGrahamRepository stockGrahamRepository;
    private final ProductRepository productRepository;
    private final InvestmentService investmentService;
    private final PortfolioService portfolioService;

    @Autowired
    public InvestmentInit(StockGrahamRepository stockGrahamRepository, ProductRepository productRepository, InvestmentService investmentService, PortfolioService portfolioService) {
        this.stockGrahamRepository = stockGrahamRepository;
        this.productRepository = productRepository;
        this.investmentService = investmentService;
        this.portfolioService = portfolioService;
    }

    /**
     * Initializes DB with dummy data iff they are not already present
     */
    public void saveInitPortfolios() {
        if(this.portfolioService.findByOwner("Carlos").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.saveCarlosMetalInvestments(), new ArrayList<>());
            this.portfolioService.save(portfolio);
        }
        if(this.portfolioService.findByOwner("Sanchez").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.saveSanchezMetalInvestments(), new ArrayList<>());
            this.portfolioService.save(portfolio);
        }
        if(this.portfolioService.findByOwner("Eduardo").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.saveEduardoMetalInvestments(), new ArrayList<>());
            this.portfolioService.save(portfolio);
        }
        if(this.portfolioService.findByOwner("Filip").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.saveEduardoMetalInvestments(), this.saveSomeStockInvestments());
            this.portfolioService.save(portfolio);
        }

//        this.portfolioService.addInvestmentToPortfolio("Carlos",
//                this.investmentService.save(
//                    new InvestmentMetal (
//                            this.productService.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(
//                                    Dealer.BESSERGOLD,
//                                    Producer.MUNZE_OSTERREICH,
//                                    Metal.GOLD,
//                                    Form.KINEBAR,
//                                    10
//                            ), 14569.00, LocalDate.of(2021, 5, 18))));
    }

    private List<InvestmentMetal> duplicateInvestmentMetals(int times, Product product, Dealer dealer, double beginPrice, LocalDate localeDate) {
        List<InvestmentMetal> result = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            result.add(
                    new InvestmentMetal(product, dealer, beginPrice, localeDate)
            );
        }
        return  result;
    }

    private List<InvestmentMetal> saveCarlosMetalInvestments() {

        //////// GOLD ////////
        //////// GOLD BAR

        InvestmentMetal goldenBar0 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.MUNZE_OSTERREICH,
                        Metal.GOLD,
                        Form.KINEBAR,
                        10,
                        2021
                ).get(),
                Dealer.BESSERGOLD_CZ,
                14569.00,
                LocalDate.of(2021, 5, 18)
        );

        InvestmentMetal goldenBar1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.MUNZE_OSTERREICH,
                        Metal.GOLD,
                        Form.BAR,
                        2,
                        2021
                ).get(),
                Dealer.BESSERGOLD_CZ,
                3164.75,
                LocalDate.of(2021, 1, 19)
        );

        InvestmentMetal goldenBar2 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.MUNZE_OSTERREICH,
                        Metal.GOLD,
                        Form.BAR,
                        2,
                        2021
                ).get(),
                Dealer.ZLATAKY,
                3547.00,
                LocalDate.of(2020, 10, 12)
        );


        //////// SILVER ////////
        //////// SILVER BAR

        Product silverBar2 =
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.ARGOR_HERAEUS,
                        Metal.SILVER,
                        Form.BAR,
                        500,
                        2021
                ).get();


        InvestmentMetal silverBar1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.ARGOR_HERAEUS,
                        Metal.SILVER,
                        Form.BAR,
                        1000,
                        2021
                ).get(),
                Dealer.BESSERGOLD_CZ,
                17832.76,
                LocalDate.of(2020, 1, 22)
        );

        InvestmentMetal silverBar21 = new InvestmentMetal(
                silverBar2,
                Dealer.BESSERGOLD_CZ,
                7752.67,
                LocalDate.of(2018, 10, 17)
        );

        InvestmentMetal silverBar22 = new InvestmentMetal(
                silverBar2,
                Dealer.BESSERGOLD_CZ,
                7375.80,
                LocalDate.of(2018, 9, 10)
        );


        //////// SILVER COIN

        Product silverWienerCoin = getWienerCoin(2021);

        List<InvestmentMetal> silverMapleCoins1 = duplicateInvestmentMetals(
                3,
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.ROYAL_CANADIAN_MINT,
                        Metal.SILVER,
                        Form.COIN,
                        TROY_OUNCE,
                        2021
                ).get(),
                Dealer.ZLATAKY,
                848.00,
                LocalDate.of(2021, 1, 25)
        );

        List<InvestmentMetal> silverWienerCoins1 = duplicateInvestmentMetals(
                2,
                silverWienerCoin,
                Dealer.ZLATAKY,
                845.00,
                LocalDate.of(2021, 1, 25)
        );

        InvestmentMetal silverWienerCoin1 = new InvestmentMetal(
                silverWienerCoin,
                Dealer.ZLATAKY,
                868.00,
                LocalDate.of(2020, 12, 10)
        );


        //////// PLATINUM ////////

        InvestmentMetal platinumBar1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.ARGOR_HERAEUS,
                        Metal.PLATINUM,
                        Form.BAR,
                        5,
                        2021
                ).get(),
                Dealer.BESSERGOLD_CZ,
                4409.18,
                LocalDate.of(2019, 2, 18)
        );

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            add(goldenBar0);
            add(goldenBar1);
            add(goldenBar2);
            add(silverBar1);
            add(silverBar21);
            add(silverBar22);
            addAll(silverMapleCoins1);
            addAll(silverWienerCoins1);
            add(silverWienerCoin1);
            add(platinumBar1);
        }};

        investmentMetalList.forEach(
                this.investmentService::save
        );

        return investmentMetalList;
    }

    private List<InvestmentMetal> saveSanchezMetalInvestments() {
        InvestmentMetal silverBar2 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD_CZ, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD_CZ, 7375.80, LocalDate.of(2018, 9, 10));
        InvestmentMetal silverBar3 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD_CZ, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD_CZ, 7375.80, LocalDate.of(2018, 9, 10));
        InvestmentMetal silverBar1 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD_CZ, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD_CZ, 7752.67, LocalDate.of(2018, 10, 17));
        InvestmentMetal silverBar4 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD_CZ, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD_CZ, 7752.67, LocalDate.of(2018, 10, 17));

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            add(silverBar1);
            add(silverBar2);
            add(silverBar3);
            add(silverBar4);
        }};

        investmentMetalList.forEach(
                this.investmentService::save
        );

        return investmentMetalList;
    }

    private List<InvestmentMetal> saveEduardoMetalInvestments() {
        Product silverWienerCoin = getWienerCoin(2021);

        List<InvestmentMetal> silverMapleCoin1 = duplicateInvestmentMetals(
                10,
                productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD_CZ,
                        Producer.ROYAL_CANADIAN_MINT,
                        Metal.SILVER,
                        Form.COIN,
                        TROY_OUNCE,
                        2021
                ).get(),
                Dealer.BESSERGOLD_CZ,
                848.00,
                LocalDate.of(2021, 1, 25)
        );

        List<InvestmentMetal> silverWienerCoins1 = duplicateInvestmentMetals(
                3,
                silverWienerCoin,
                Dealer.BESSERGOLD_CZ,
                868.00,
                LocalDate.of(2020, 12, 10)
        );

        List<InvestmentMetal> silverWienerCoins2 = duplicateInvestmentMetals(
                3,
                silverWienerCoin,
                Dealer.BESSERGOLD_CZ,
                845.00,
                LocalDate.of(2021, 1, 25)
        );

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            addAll(silverMapleCoin1);
            addAll(silverWienerCoins1);
            addAll(silverWienerCoins2);
        }};

        investmentMetalList.forEach(
                this.investmentService::save
        );

        return investmentMetalList;
    }

    private List<InvestmentStock> saveSomeStockInvestments() {
        StockGraham stockGraham = this.stockGrahamRepository.findByTicker_Ticker("RINO").get();
        StockGraham stockGraham1 = this.stockGrahamRepository.findByTicker_Ticker("NC").get();
        StockGraham stockGraham2 = this.stockGrahamRepository.findByTicker_Ticker("CMC").get();

        InvestmentStock silverBar1 = new InvestmentStock(stockGraham, 10, stockGraham.getPreviousClose(), LocalDate.of(2018, 9, 10));
        InvestmentStock silverBar2 = new InvestmentStock(stockGraham1, 10, stockGraham1.getPreviousClose(), LocalDate.of(2018, 9, 10));
        InvestmentStock silverBar3 = new InvestmentStock(stockGraham2, 100, stockGraham2.getPreviousClose(), LocalDate.of(2018, 9, 10));


        List<InvestmentStock> investmentMetalList = new ArrayList<>() {{
            add(silverBar1);
            add(silverBar2);
            add(silverBar3);
        }};

        investmentMetalList.forEach(
                this.investmentService::save
        );

        return investmentMetalList;
    }

    private Product getWienerCoin(int year) {
        return this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                Dealer.BESSERGOLD_CZ,
                Producer.MUNZE_OSTERREICH,
                Metal.SILVER,
                Form.COIN,
                TROY_OUNCE,
                year
        ).get();
    }

}

package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Enum.Form;
import home.holymiko.ScrapApp.Server.Enum.Metal;
import home.holymiko.ScrapApp.Server.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.InvestmentMetal;
import home.holymiko.ScrapApp.Server.Repository.InvestmentRepository;
import home.holymiko.ScrapApp.Server.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvestmentService {

    private final ProductRepository productRepository;
    private final InvestmentRepository investmentRepository;

    private final ProductService productService;

    private static final double TROY_OUNCE = 31.1034768;

    @Autowired
    public InvestmentService(ProductRepository productRepository, InvestmentRepository investmentRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.investmentRepository = investmentRepository;
        this.productService = productService;
    }


    ////// FIND

//    public List<InvestmentMetal> longToInvestments(List<Long> investmentIds) {
//        List<InvestmentMetal> investmentMetals = new ArrayList<>();
//        for (Long id:
//                investmentIds) {
//            Optional<InvestmentMetal> optionalInvestment = this.investmentRepository.findById(id);
//            if(optionalInvestment.isPresent()){
//                investmentMetals.add(optionalInvestment.get());
//            } else {
//                System.out.println("InvestmentMetal by ID does exist");
//            }
//        }
//        return investmentMetals;
//    }
//
//    public Optional<InvestmentMetal> findById(Long id) {
//        return this.investmentRepository.findById(id);
//    }


    ////// POST

//    @Transactional
//    public InvestmentMetal save(Product product) throws ResponseStatusException {
//        Optional<Product> optionalProduct = this.productRepository.findById(product.getId());
//        if(optionalProduct.isPresent()){
//            System.out.println("localDate");
//            InvestmentMetal investmentMetal = new InvestmentMetal( product,  LocalDate.of(100,1,1));
//            this.investmentRepository.save(investmentMetal);
//            return this.investmentRepository.findById(investmentMetal.getId()).get();
//        }
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with such ID not found");
//    }

    @Transactional
    public InvestmentMetal save(InvestmentMetal investmentMetal) throws ResponseStatusException {
        System.out.println("localDate");
        this.investmentRepository.save(investmentMetal);
        return this.investmentRepository.findById(investmentMetal.getId()).get();
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

    @Transactional
    public List<InvestmentMetal> saveCarlosInvestments() {

        //////// GOLD ////////
        //////// GOLD BAR

        InvestmentMetal goldenBar0 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.MUNZE_OSTERREICH,
                        Metal.GOLD,
                        Form.KINEBAR,
                        10,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
                14569.00,
                LocalDate.of(2021, 5, 18)
        );

        InvestmentMetal goldenBar1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.MUNZE_OSTERREICH,
                        Metal.GOLD,
                        Form.BAR,
                        2,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
                3164.75,
                LocalDate.of(2021, 1, 19)
        );

        InvestmentMetal goldenBar2 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
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
                        Dealer.BESSERGOLD,
                        Producer.ARGOR_HERAEUS,
                        Metal.SILVER,
                        Form.BAR,
                        500,
                        2021
                ).get();


        InvestmentMetal silverBar1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.ARGOR_HERAEUS,
                        Metal.SILVER,
                        Form.BAR,
                        1000,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
                17832.76,
                LocalDate.of(2020, 1, 22)
        );

        InvestmentMetal silverBar21 = new InvestmentMetal(
                silverBar2,
                Dealer.BESSERGOLD,
                7752.67,
                LocalDate.of(2018, 10, 17)
        );

        InvestmentMetal silverBar22 = new InvestmentMetal(
                silverBar2,
                Dealer.BESSERGOLD,
                7375.80,
                LocalDate.of(2018, 9, 10)
        );


        //////// SILVER COIN

        Product silverWienerCoin = getWienerCoin(2021);

        List<InvestmentMetal> silverMapleCoins1 = duplicateInvestmentMetals(
                    3,
                    this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                            Dealer.BESSERGOLD,
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
                        Dealer.BESSERGOLD,
                        Producer.ARGOR_HERAEUS,
                        Metal.PLATINUM,
                        Form.BAR,
                        5,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
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
                this.investmentRepository::save
        );

        return investmentMetalList;
    }

    @Transactional
    public List<InvestmentMetal> saveSanchezInvestments() {
        InvestmentMetal silverBar2 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7375.80, LocalDate.of(2018, 9, 10));
        InvestmentMetal silverBar3 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7375.80, LocalDate.of(2018, 9, 10));
        InvestmentMetal silverBar1 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7752.67, LocalDate.of(2018, 10, 17));
        InvestmentMetal silverBar4 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7752.67, LocalDate.of(2018, 10, 17));

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            add(silverBar1);
            add(silverBar2);
            add(silverBar3);
            add(silverBar4);
        }};

        investmentMetalList.forEach(
                this.investmentRepository::save
        );

        return investmentMetalList;
    }

    @Transactional
    public List<InvestmentMetal> saveEduardoInvestments() {
        Product silverWienerCoin = getWienerCoin(2021);

        List<InvestmentMetal> silverMapleCoin1 = duplicateInvestmentMetals(
                10,
                productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.ROYAL_CANADIAN_MINT,
                        Metal.SILVER,
                        Form.COIN,
                        TROY_OUNCE,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
                848.00,
                LocalDate.of(2021, 1, 25)
        );

        List<InvestmentMetal> silverWienerCoins1 = duplicateInvestmentMetals(
                3,
                silverWienerCoin,
                Dealer.BESSERGOLD,
                868.00,
                LocalDate.of(2020, 12, 10)
        );

        List<InvestmentMetal> silverWienerCoins2 = duplicateInvestmentMetals(
                3,
                silverWienerCoin,
                Dealer.BESSERGOLD,
                845.00,
                LocalDate.of(2021, 1, 25)
        );

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            addAll(silverMapleCoin1);
            addAll(silverWienerCoins1);
            addAll(silverWienerCoins2);
        }};

        investmentMetalList.forEach(
                this.investmentRepository::save
        );

        return investmentMetalList;
    }

    private Product getWienerCoin(int year) {
        return this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                Dealer.BESSERGOLD,
                Producer.MUNZE_OSTERREICH,
                Metal.SILVER,
                Form.COIN,
                TROY_OUNCE,
                year
        ).get();
    }
}

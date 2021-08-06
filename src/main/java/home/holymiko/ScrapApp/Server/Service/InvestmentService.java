package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.simple.InvestmentMetalDTO;
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
import java.time.Year;
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


    public InvestmentMetalDTO toDTO(InvestmentMetal investmentMetal){
        return new InvestmentMetalDTO(
                investmentMetal.getId(),
                productService.toDTOLatestPrices(investmentMetal.getProduct()),
                investmentMetal.getDealer(),
                getYield(investmentMetal),
                investmentMetal.getBeginPrice(),
                investmentMetal.getEndPrice(),
                investmentMetal.getBeginDate(),
                investmentMetal.getEndDate()
        );
    }

    public double getYield(InvestmentMetal investmentMetal) {
        return investmentMetal.getProduct().getLatestPriceByDealer(
                investmentMetal.getDealer()
        ).getRedemption() / investmentMetal.getBeginPrice();
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

    @Transactional
    public List<InvestmentMetal> saveCarlosInvestments() {

        // GOLD BAR

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


        // SILVER BAR

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

        InvestmentMetal silverBar2 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.ARGOR_HERAEUS,
                        Metal.SILVER,
                        Form.BAR,
                        500,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
                7752.67,
                LocalDate.of(2018, 10, 17)
        );

        InvestmentMetal silverBar3 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.ARGOR_HERAEUS,
                        Metal.SILVER,
                        Form.BAR,
                        500,
                        2021
                ).get(),
                Dealer.BESSERGOLD,
                7375.80,
                LocalDate.of(2018, 9, 10)
        );


        // SILVER COIN

        InvestmentMetal silverMapleCoin1 = new InvestmentMetal(
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

        InvestmentMetal silverMapleCoin2 = new InvestmentMetal(
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

        InvestmentMetal silverMapleCoin3 = new InvestmentMetal(
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

        InvestmentMetal silverWienerCoin1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.MUNZE_OSTERREICH,
                        Metal.SILVER,
                        Form.COIN,
                        TROY_OUNCE,
                        2021
                ).get(),
                Dealer.ZLATAKY,
                868.00,
                LocalDate.of(2020, 12, 10)
        );

        InvestmentMetal silverWienerCoin2 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.MUNZE_OSTERREICH,
                        Metal.SILVER,
                        Form.COIN,
                        TROY_OUNCE,
                        2021
                ).get(),
                Dealer.ZLATAKY,
                845.00,
                LocalDate.of(2021, 1, 25)
        );

        InvestmentMetal silverWienerCoin3 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(
                        Dealer.BESSERGOLD,
                        Producer.MUNZE_OSTERREICH,
                        Metal.SILVER,
                        Form.COIN,
                        TROY_OUNCE,
                        2021
                ).get(),
                Dealer.ZLATAKY,
                845.00,
                LocalDate.of(2021, 1, 25)
        );


        // PLATINUM

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
            add(silverBar2);
            add(silverBar3);
            add(platinumBar1);
            add(silverMapleCoin1);
            add(silverMapleCoin2);
            add(silverMapleCoin3);
            add(silverWienerCoin1);
            add(silverWienerCoin2);
            add(silverWienerCoin3);
        }};

        this.investmentRepository.save(goldenBar0);
        this.investmentRepository.save(goldenBar1);
        this.investmentRepository.save(goldenBar2);
        this.investmentRepository.save(silverBar1);
        this.investmentRepository.save(silverBar2);
        this.investmentRepository.save(silverBar3);
        this.investmentRepository.save(platinumBar1);
        this.investmentRepository.save(silverMapleCoin1);
        this.investmentRepository.save(silverMapleCoin2);
        this.investmentRepository.save(silverMapleCoin3);
        this.investmentRepository.save(silverWienerCoin1);
        this.investmentRepository.save(silverWienerCoin2);
        this.investmentRepository.save(silverWienerCoin3);
        return investmentMetalList;
    }

    @Transactional
    public List<InvestmentMetal> saveSanchezInvestments() {
        InvestmentMetal silverBar1 = new InvestmentMetal(
                this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7752.67, LocalDate.of(2018, 10, 17));
        InvestmentMetal silverBar2 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7375.80, LocalDate.of(2018, 9, 10));
        InvestmentMetal silverBar3 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7375.80, LocalDate.of(2018, 9, 10));
        InvestmentMetal silverBar4 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500, 2021).get(), Dealer.BESSERGOLD, 7752.67, LocalDate.of(2018, 10, 17));

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            add(silverBar1);
            add(silverBar2);
            add(silverBar3);
            add(silverBar4);
        }};

        this.investmentRepository.save(silverBar1);
        this.investmentRepository.save(silverBar2);
        this.investmentRepository.save(silverBar3);
        this.investmentRepository.save(silverBar4);
        return investmentMetalList;
    }

    @Transactional
    public List<InvestmentMetal> saveEduardoInvestments() {
        InvestmentMetal silverMapleCoin1 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin2 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin3 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin4 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin5 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin6 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin7 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin8 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin9 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverMapleCoin10 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 848.00, LocalDate.of(2021, 1, 25));


        InvestmentMetal silverWienerCoin1 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 868.00, LocalDate.of(2020, 12, 10));
        InvestmentMetal silverWienerCoin2 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverWienerCoin3 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverWienerCoin4 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 868.00, LocalDate.of(2020, 12, 10));
        InvestmentMetal silverWienerCoin5 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverWienerCoin6 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverWienerCoin7 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 868.00, LocalDate.of(2020, 12, 10));
        InvestmentMetal silverWienerCoin8 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverWienerCoin9 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));
        InvestmentMetal silverWienerCoin10 = new InvestmentMetal(this.productRepository.findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE, 2021).get(), Dealer.BESSERGOLD, 845.00, LocalDate.of(2021, 1, 25));

        List<InvestmentMetal> investmentMetalList = new ArrayList<>() {{
            add(silverMapleCoin1);
            add(silverMapleCoin2);
            add(silverMapleCoin3);
            add(silverMapleCoin4);
            add(silverMapleCoin5);
            add(silverMapleCoin6);
            add(silverMapleCoin7);
            add(silverMapleCoin8);
            add(silverMapleCoin9);
            add(silverMapleCoin10);

            add(silverWienerCoin1);
            add(silverWienerCoin2);
            add(silverWienerCoin3);
            add(silverWienerCoin4);
            add(silverWienerCoin5);
            add(silverWienerCoin6);
            add(silverWienerCoin7);
            add(silverWienerCoin8);
            add(silverWienerCoin9);
            add(silverWienerCoin10);
        }};

        this.investmentRepository.save(silverMapleCoin1);
        this.investmentRepository.save(silverMapleCoin2);
        this.investmentRepository.save(silverMapleCoin3);
        this.investmentRepository.save(silverMapleCoin4);
        this.investmentRepository.save(silverMapleCoin5);
        this.investmentRepository.save(silverMapleCoin6);
        this.investmentRepository.save(silverMapleCoin7);
        this.investmentRepository.save(silverMapleCoin8);
        this.investmentRepository.save(silverMapleCoin9);
        this.investmentRepository.save(silverMapleCoin10);

        this.investmentRepository.save(silverWienerCoin1);
        this.investmentRepository.save(silverWienerCoin2);
        this.investmentRepository.save(silverWienerCoin3);
        this.investmentRepository.save(silverWienerCoin4);
        this.investmentRepository.save(silverWienerCoin5);
        this.investmentRepository.save(silverWienerCoin6);
        this.investmentRepository.save(silverWienerCoin7);
        this.investmentRepository.save(silverWienerCoin8);
        this.investmentRepository.save(silverWienerCoin9);
        this.investmentRepository.save(silverWienerCoin10);

        return investmentMetalList;
    }
}

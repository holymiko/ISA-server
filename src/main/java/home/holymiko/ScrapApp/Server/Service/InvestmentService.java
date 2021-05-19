package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.InvestmentDTO;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Repository.InvestmentRepository;
import home.holymiko.ScrapApp.Server.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public InvestmentDTO toDTO(Investment investment){
        return new InvestmentDTO(
                investment.getId(),
                productService.toDTO(investment.getProduct()),
                investment.getYield(),
                investment.getBeginPrice(),
                investment.getEndPrice(),
                investment.getBeginDate(),
                investment.getEndDate()
        );
    }


    ////// FIND

    public List<Investment> longToInvestments(List<Long> investmentIds) {
        List<Investment> investments = new ArrayList<>();
        for (Long id:
                investmentIds) {
            Optional<Investment> optionalInvestment = this.investmentRepository.findById(id);
            if(optionalInvestment.isPresent()){
                investments.add(optionalInvestment.get());
            } else {
                System.out.println("Investment by ID does exist");
            }
        }
        return investments;
    }

    public Optional<Investment> findById(Long id) {
        return this.investmentRepository.findById(id);
    }


    ////// POST

    @Transactional
    public Investment save(Product product) throws ResponseStatusException {
        Optional<Product> optionalProduct = this.productRepository.findById(product.getId());
        if(optionalProduct.isPresent()){
            System.out.println("localDate");
            Investment investment = new Investment( product, LocalDate.of(100,1,1));
            this.investmentRepository.save(investment);
            return this.investmentRepository.findById(investment.getId()).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with such ID not found");
    }

    @Transactional
    public Investment save(Investment investment) throws ResponseStatusException {
        System.out.println("localDate");
        this.investmentRepository.save(investment);
        return this.investmentRepository.findById(investment.getId()).get();
    }

    @Transactional
    public List<Investment> saveCarlosInvestments() {
        Investment goldenBar1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.GOLD, Form.BAR, 2).get(), 3164.75, LocalDate.of(2021, 1, 19));
        Investment goldenBar2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.GOLD, Form.BAR, 2).get(), 3547.00, LocalDate.of(2020, 10, 12));

        Investment silverBar1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 1000).get(), 17832.76, LocalDate.of(2020, 1, 22));
        Investment silverBar2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500).get(), 7752.67, LocalDate.of(2018, 10, 17));
        Investment silverBar3 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500).get(), 7375.80, LocalDate.of(2018, 9, 10));

        Investment silverMapleCoin1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin3 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));

        Investment silverWienerCoin1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 868.00, LocalDate.of(2020, 12, 10));
        Investment silverWienerCoin2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin3 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));

        Investment platinumBar1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.PLATINUM, Form.BAR, 5).get(), 4409.18, LocalDate.of(2019, 2, 18));

        List<Investment> investmentList = new ArrayList<>() {{
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
        return investmentList;
    }

    @Transactional
    public List<Investment> saveSanchezInvestments() {
        Investment silverBar1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500).get(), 7752.67, LocalDate.of(2018, 10, 17));
        Investment silverBar2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500).get(), 7375.80, LocalDate.of(2018, 9, 10));
        Investment silverBar3 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500).get(), 7375.80, LocalDate.of(2018, 9, 10));
        Investment silverBar4 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ARGOR_HERAEUS, Metal.SILVER, Form.BAR, 500).get(), 7752.67, LocalDate.of(2018, 10, 17));

        List<Investment> investmentList = new ArrayList<>() {{
            add(silverBar1);
            add(silverBar2);
            add(silverBar3);
            add(silverBar4);
        }};

        this.investmentRepository.save(silverBar1);
        this.investmentRepository.save(silverBar2);
        this.investmentRepository.save(silverBar3);
        this.investmentRepository.save(silverBar4);
        return investmentList;
    }

    @Transactional
    public List<Investment> saveEduardoInvestments() {
        Investment silverMapleCoin1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin3 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin4 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin5 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin6 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin7 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin8 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin9 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin10 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.ROYAL_CANADIAN_MINT, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 848.00, LocalDate.of(2021, 1, 25));


        Investment silverWienerCoin1 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 868.00, LocalDate.of(2020, 12, 10));
        Investment silverWienerCoin2 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin3 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin4 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 868.00, LocalDate.of(2020, 12, 10));
        Investment silverWienerCoin5 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin6 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin7 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 868.00, LocalDate.of(2020, 12, 10));
        Investment silverWienerCoin8 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin9 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin10 = new Investment(this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer.BESSERGOLD, Producer.MUNZE_OSTERREICH, Metal.SILVER, Form.COIN, TROY_OUNCE).get(), 845.00, LocalDate.of(2021, 1, 25));

        List<Investment> investmentList = new ArrayList<>() {{
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

        return investmentList;
    }
}

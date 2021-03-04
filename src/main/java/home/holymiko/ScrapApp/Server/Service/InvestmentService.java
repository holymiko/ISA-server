package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.InvestmentDTO;
import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Repository.InvestmentRepository;
import home.holymiko.ScrapApp.Server.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvestmentService {

    private final ProductRepository productRepository;
    private final InvestmentRepository investmentRepository;

    private final ProductService productService;

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

    @Transactional
    public List<Investment> saveMyInvestments() {
        Investment goldenBar1 = new Investment(this.productRepository.findById(2932L).get(), 3164.75, LocalDate.of(2021, 1, 19));
        Investment goldenBar2 = new Investment(this.productRepository.findById(2932L).get(), 3547.00, LocalDate.of(2020, 10, 12));

        Investment silverBar1 = new Investment(this.productRepository.findById(3124L).get(), 17832.76, LocalDate.of(2020, 1, 22));
        Investment silverBar2 = new Investment(this.productRepository.findById(3123L).get(), 7752.67, LocalDate.of(2018, 10, 17));
        Investment silverBar3 = new Investment(this.productRepository.findById(3123L).get(), 7375.80, LocalDate.of(2018, 9, 10));

        Investment silverMapleCoin1 = new Investment(this.productRepository.findById(3130L).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin2 = new Investment(this.productRepository.findById(3130L).get(), 848.00, LocalDate.of(2021, 1, 25));
        Investment silverMapleCoin3 = new Investment(this.productRepository.findById(3130L).get(), 848.00, LocalDate.of(2021, 1, 25));

        Investment silverWienerCoin1 = new Investment(this.productRepository.findById(3129L).get(), 868.00, LocalDate.of(2020, 12, 10));
        Investment silverWienerCoin2 = new Investment(this.productRepository.findById(3129L).get(), 845.00, LocalDate.of(2021, 1, 25));
        Investment silverWienerCoin3 = new Investment(this.productRepository.findById(3129L).get(), 845.00, LocalDate.of(2021, 1, 25));

        Investment platinumBar1 = new Investment(this.productRepository.findById(3165L).get(), 4409.18, LocalDate.of(2019, 2, 18));

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
}

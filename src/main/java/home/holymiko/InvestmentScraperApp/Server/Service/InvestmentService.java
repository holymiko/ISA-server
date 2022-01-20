package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Entity.InvestmentMetal;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.InvestmentRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
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


    @Autowired
    public InvestmentService(ProductRepository productRepository, InvestmentRepository investmentRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.investmentRepository = investmentRepository;
        this.productService = productService;
    }


    @Transactional
    public InvestmentMetal save(InvestmentMetal investmentMetal) throws ResponseStatusException {
        this.investmentRepository.save(investmentMetal);
        return this.investmentRepository.findById(investmentMetal.getId()).get();
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

}

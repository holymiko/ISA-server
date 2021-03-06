package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.InvestmentStockRepository;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.InvestmentMetal;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.InvestmentMetalRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.InvestmentStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InvestmentService {

    private final ProductRepository productRepository;
    private final InvestmentMetalRepository investmentMetalRepository;
    private final InvestmentStockRepository investmentStockRepository;
    private final ProductService productService;

    @Autowired
    public InvestmentService(ProductRepository productRepository, InvestmentMetalRepository investmentMetalRepository, InvestmentStockRepository investmentStockRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.investmentMetalRepository = investmentMetalRepository;
        this.investmentStockRepository = investmentStockRepository;
        this.productService = productService;
    }


    @Transactional
    public InvestmentMetal save(InvestmentMetal investmentMetal) throws ResponseStatusException {
        return this.investmentMetalRepository.save(investmentMetal);
    }

    @Transactional
    public InvestmentStock save(InvestmentStock investmentMetal) throws ResponseStatusException {
        return this.investmentStockRepository.save(investmentMetal);
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

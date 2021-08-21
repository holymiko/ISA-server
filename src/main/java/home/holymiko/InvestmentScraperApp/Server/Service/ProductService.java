package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.DTO.toDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /////////// FIND AS DTO

    public Optional<ProductDTO_LatestPrices> findByIdAsDTO(Long id) {
        Optional<Product> optionalProductDTO = this.productRepository.findById(id);
        return optionalProductDTO.map(toDTO::toDTO_LatestPrices);
    }

    public Optional<ProductDTO_AllPrices> findByIdAsDTOAllPrices(Long id) {
        return toDTO.toDTO_AllPrices(this.productRepository.findById(id));
    }

    public List<ProductDTO_LatestPrices> findAllAsDTO() {
        return productRepository.findAll()
                .stream()
                .filter(product ->
                    product.getGrams() >= 0 //&&
//                    product.getLatestPrices().getPrice() >= 0     TODO
                )
                .map(toDTO::toDTO_LatestPrices)
                .collect(Collectors.toList());
    }

    public List<ProductDTO_LatestPrices> findByMetalAsDTO(Metal metal) {
        return productRepository.findProductsByMetal(metal)
                .stream()
                .map(toDTO::toDTO_LatestPrices)
                .collect(Collectors.toList());
    }


    /////////// FIND

    public List<Product> findByMetal(Metal metal) {
        return this.productRepository.findProductsByMetal(metal);
    }

    public Optional<Product> findByLink(String link) {
        return this.productRepository.findByLinks_Link(link);
    }

    public List<Product> findProductByProducerAndMetalAndFormAndGramsAndYear(Producer producer, Metal metal, Form form, double grams, int year) {
        return this.productRepository.findProductByProducerAndMetalAndFormAndGramsAndYear(producer, metal, form, grams, year);
    }

//    public Optional<Product> findById(Long id) {
//        return this.productRepository.findById(id);
//    }

//    public Optional<Product> findByLink(Link link) {
//        return this.productRepository.findByLinks(link);
//    }

//    public List<Product> findAll() {
//        return this.productRepository.findAll();
//    }

//    public List<Product> findProducts(List<Long> investmentIds) {
//        List<Product> investments = new ArrayList<>();
//        for (Long id : investmentIds) {
//            Optional<Product> optionalProduct = this.productRepository.findById(id);
//            if(optionalProduct.isPresent()){
//                investments.add(optionalProduct.get());
//            } else {
//                System.out.println("Product by ID does exist");
//            }
//        }
//        return investments;
//    }

//    public List<Price> findProductPrices(Long id) {
//        Optional<Product> product = this.productRepository.findById(id);
//        if (product.isPresent()) {
//            return product.get().getPrices();
//        }
//        return new ArrayList<>();
//    }

    /////////// SAVE

    @Transactional
    public void save(Product product) {
        this.productRepository.save(product);
    }

}

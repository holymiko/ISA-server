package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.ProductDTO;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    //////// toDTO

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(),
                product.getMetalString(),
                product.getName(),
                product.getGrams(),
                product.getLink().getLink(),
                product.getLatestPrice(),
                product.getPrices().stream()
                        .map(Price::getDateTime)
                        .collect(Collectors.toList())
        );
    }

    public Optional<ProductDTO> toDTO(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(optionalProduct.get()));
    }


    /////////// FIND AS DTO

    public Optional<ProductDTO> findByIdAsDTO(Long id) {
        Optional<Product> optionalProductDTO = this.productRepository.findById(id);
        return optionalProductDTO.map(this::toDTO);
    }

    public List<ProductDTO> findAllAsDTO() {
        return productRepository.findAll()
                .stream()
                .filter(product ->
                    product.getGrams() >= 0 &&
                    product.getLatestPrice().getPrice() >= 0
                )
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> findByMetalAsDTO(Metal metal) {
        return productRepository.findProductsByMetal(metal).stream().map(this::toDTO).collect(Collectors.toList());
    }


    /////////// FIND

    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    public Optional<Product> findByLink(Link link) {
        return this.productRepository.findByLink(link);
    }

    public Optional<Product> findByLink(String link) {
        return this.productRepository.findByLink_Link(link);
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public List<Product> findByMetal(Metal metal) {
        return this.productRepository.findProductsByMetal(metal);
    }

    public List<Product> findProducts(List<Long> investmentIds) {
        List<Product> investments = new ArrayList<>();
        for (Long id:
                investmentIds) {
            Optional<Product> optionalProduct = this.productRepository.findById(id);
            if(optionalProduct.isPresent()){
                investments.add(optionalProduct.get());
            } else {
                System.out.println("Product by ID does exist");
            }
        }
        return investments;
    }

    public Product findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(Dealer dealer, Producer producer, Metal metal, Form form, double grams) {
        Optional<Product> optional = this.productRepository.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(dealer, producer, metal, form, grams);
        return optional.orElse(null);           // Returns optional if its present
    }

    public List<Price> findProductPrices(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get().getPrices();
        }
        return new ArrayList<>();
    }

    /////////// SAVE

    @Transactional
    public void save(Product product) {
        this.productRepository.save(product);
    }

}

package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.ProductDTO;
import home.holymiko.ScrapApp.Server.Entity.*;
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
                product.getName(), product.getGrams(),
                product.getLink().getLink(), product.getLatestPrice(),
                product.getPrices().stream().map(Price::getDateTime).collect(Collectors.toList())
        );
    }

    public Optional<ProductDTO> toDTO(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(optionalProduct.get()));
    }



    /////////// GET

    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    public Optional<ProductDTO> findByIdAsDTO(Long id) {
        Optional<Product> optionalProductDTO = this.productRepository.findById(id);
        return optionalProductDTO.map(this::toDTO);
    }

    public List<Product> findByLink(Link link) {
        return this.productRepository.findByLink(link);
    }

    public List<Product> findByLink(String link) {
        return this.productRepository.findByLink_Link(link);
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public List<ProductDTO> findAllAsDTO() {
        return productRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }


    public List<Product> findByMetal(Metal metal) {
        return this.productRepository.findProductsByMetal(metal);
    }

    public List<ProductDTO> findByMetalAsDTO(Metal metal) {
        return productRepository.findProductsByMetal(metal).stream().map(this::toDTO).collect(Collectors.toList());
    }

//    public List<Product> findByMetal(double max) {
//        return this.productRepository.findByMetal(max);
//    }

    public List<Product> getSilverProducts() {
        return this.productRepository.findProductsByMetal(Metal.SILVER);
    }

    public List<Product> getPlatinumProducts() {
        return this.productRepository.findProductsByMetal(Metal.PLATINUM);
    }

    public List<Price> getProductPrices(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get().getPrices();
        }
        return new ArrayList<>();
    }


    /////////// POST

    @Transactional
    public void save(Product product) {
        this.productRepository.save(product);
    }

}

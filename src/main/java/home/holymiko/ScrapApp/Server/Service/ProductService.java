package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.ScrapApp.Server.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Enum.Form;
import home.holymiko.ScrapApp.Server.Enum.Metal;
import home.holymiko.ScrapApp.Server.Enum.Producer;
import home.holymiko.ScrapApp.Server.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
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

    public ProductDTO_LatestPrices toDTOLatestPrices(Product product) {
        return new ProductDTO_LatestPrices(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                product.getLinks()
                        .stream()
                        .map(Link::getLink)
                        .collect(Collectors.toList()),
                product.getLatestPrices()
        );
    }

    public ProductDTO_AllPrices toDTOAllPrices(Product product) {
        return new ProductDTO_AllPrices(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                product.getLinks()
                        .stream()
                        .map(Link::getLink)
                        .collect(Collectors.toList()),
                product.getLatestPrices(),
                product.getPrices()
        );
    }

    public Optional<ProductDTO_AllPrices> toDTOAllPrices(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                toDTOAllPrices(optionalProduct.get())
        );
    }

    public Optional<ProductDTO_LatestPrices> toDTOLatestPrices(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                toDTOLatestPrices(optionalProduct.get())
        );
    }




    /////////// FIND AS DTO

    public Optional<ProductDTO_LatestPrices> findByIdAsDTO(Long id) {
        Optional<Product> optionalProductDTO = this.productRepository.findById(id);
        return optionalProductDTO.map(this::toDTOLatestPrices);
    }

    public Optional<ProductDTO_AllPrices> findByIdAsDTOAllPrices(Long id) {
        return toDTOAllPrices(this.productRepository.findById(id));
    }

    public List<ProductDTO_LatestPrices> findAllAsDTO() {
        return productRepository.findAll()
                .stream()
                .filter(product ->
                    product.getGrams() >= 0 //&&
//                    product.getLatestPrices().getPrice() >= 0     TODO
                )
                .map(this::toDTOLatestPrices)
                .collect(Collectors.toList());
    }

    public List<ProductDTO_LatestPrices> findByMetalAsDTO(Metal metal) {
        return productRepository.findProductsByMetal(metal).stream().map(this::toDTOLatestPrices).collect(Collectors.toList());
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

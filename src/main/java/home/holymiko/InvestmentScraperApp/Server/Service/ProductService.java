package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Mapper.ProductMapper;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /////////// FIND AS DTO

    public Optional<ProductDTO_LatestPrices> findByIdAsDTO(Long id) {
        Optional<Product> optionalProductDTO = this.productRepository.findById(id);
        return optionalProductDTO.map(
                productMapper::toProductDTO_LatestPrices
        );
    }

    public Optional<ProductDTO_AllPrices> findByIdAsDTOAllPrices(Long id) {
        Optional<Product> optionalProduct = this.productRepository.findById(id);

        if(optionalProduct.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(productMapper.toProductDTO_AllPrices(optionalProduct.get()));
    }

    public List<ProductDTO_LatestPrices> findAllAsDTO() {
        return productRepository.findAll()
                .stream()
                .filter(product ->
                    product.getGrams() >= 0 //&&
//                    product.getLatestPrices().getPrice() >= 0     TODO
                )
                .map(productMapper::toProductDTO_LatestPrices)
                .collect(Collectors.toList());
    }

    public List<ProductDTO_LatestPrices> findByMetalAsDTO(Metal metal) {
        return productRepository.findProductsByMetal(metal)
                .stream()
                .map(productMapper::toProductDTO_LatestPrices)
                .collect(Collectors.toList());
    }


    /////////// FIND

    public List<ProductDTO_LatestPrices> findByMetal(Metal metal) {
        return this.productRepository.findProductsByMetal(metal)
                .stream()
                .map(
                        productMapper::toProductDTO_LatestPrices
                )
                .collect(Collectors.toList());
    }

    public Optional<Product> findByLink(String link) {
        return this.productRepository.findByLinks_Url(link);
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
        if(product == null) {
            throw new NullPointerException();
        }
        this.productRepository.save(product);
    }

    /**
     * Price is added to Product. LatestPrice is updated.
     * @param product Product where the Price gonna be added
     * @param price Price already saved in DB, which should be added to Product
     * @throws NullPointerException For null value in one of the parameters
     */
    @Transactional
    public void updatePrice(@NotNull Product product, @NotNull Price price) throws NullPointerException {
        final List<Price> priceList;

        if(product == null || price == null) {
            throw new NullPointerException();
        }
        priceList = product.getPrices();
        priceList.add(price);
        product.setLatestPrice(price);
        product.setPrices(priceList);
        this.productRepository.save(product);
    }
}

package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Producer;
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

    public Optional<Product> findByLink(String url) {
        return this.productRepository.findByLinks_Url(url);
    }

    public List<Product> findByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean isSpecial) {
        return this.productRepository.findByParams(dealer, producer, metal, form, grams, year, isSpecial);
    }

    public List<Product> findByParams(Dealer dealer, ProductCreateDTO product) {
        return this.productRepository.findByParams(dealer, product.getProducer(), product.getMetal(), product.getForm(), product.getGrams(), product.getYear(), product.isSpecial());
    }

    /////////// SAVE

    @Transactional
    public void save(@NotNull Product product) throws NullPointerException {
        if(product == null) {
            throw new NullPointerException();
        }
        this.productRepository.save(product);
    }

    /**
     * Price is added to Product. LatestPrice is updated.
     * Product is saved.
     * @param productId Product where the Price gonna be added
     * @param price Price already saved in DB, which should be added to Product
     * @throws NullPointerException For null value in one of the parameters
     */
    @Transactional
    public void updatePrice(@NotNull Long productId, @NotNull Price price) throws NullPointerException, IllegalArgumentException {
        final Product product;
        final List<Price> priceList;

        if(productId == null) {
            throw new NullPointerException("ProductId cannot be null");
        }
        if(price == null) {
            throw new NullPointerException("Price cannot be null");
        }

        product = productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);

        priceList = product.getPrices();
        priceList.add(price);
        product.setLatestPrice(price);
        product.setPrices(priceList);
        this.productRepository.save(product);
    }



/*
    public Optional<Product> findByLink(Link link) {
        return this.productRepository.findByLinks(link);
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public List<Product> findProducts(List<Long> investmentIds) {
        List<Product> investments = new ArrayList<>();
        for (Long id : investmentIds) {
            Optional<Product> optionalProduct = this.productRepository.findById(id);
            if(optionalProduct.isPresent()){
                investments.add(optionalProduct.get());
            } else {
                System.out.println("Product by ID does exist");
            }
        }
        return investments;
    }

    public List<Price> findProductPrices(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get().getPrices();
        }
        return new ArrayList<>();
    }

 */
}

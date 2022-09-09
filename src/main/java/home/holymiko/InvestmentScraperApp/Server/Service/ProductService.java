package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
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
    private final PricePairRepository pricePairRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, PricePairRepository pricePairRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.pricePairRepository = pricePairRepository;
    }

    /////////// FIND AS DTO

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<ProductDTO_LatestPrices> findByIdAsDTO(Long id) {
        Optional<Product> optionalProductDTO = this.productRepository.findById(id);
        return optionalProductDTO.map(
                x -> productMapper.toProductDTO_LatestPrices(x, pricePairRepository)
        );
    }

    public Optional<ProductDTO_AllPrices> findByIdAsDTOAllPrices(Long id) {
        Optional<Product> optionalProduct = this.productRepository.findById(id);

        if(optionalProduct.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(productMapper.toProductDTO_AllPrices(optionalProduct.get(), pricePairRepository));
    }

    public List<ProductDTO_LatestPrices> findAllAsDTO() {
        return productRepository.findAll()
                .stream()
                .filter(product ->
                    product.getGrams() >= 0 //&&
//                    product.getLatestPricePairs().getPrice() >= 0     TODO
                )
                .map(x -> productMapper.toProductDTO_LatestPrices(x, pricePairRepository))
                .collect(Collectors.toList());
    }

    public List<ProductDTO_LatestPrices> findByMetalAsDTO(Metal metal) {
        return productRepository.findProductsByMetal(metal)
                .stream()
                .map(x -> productMapper.toProductDTO_LatestPrices(x, pricePairRepository))
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
     * PricePair is added to Product. LatestPrice is updated.
     * Product is saved.
     * @param productId Product where the PricePair gonna be added
     * @param pricePair PricePair already saved in DB, which should be added to Product
     * @throws NullPointerException For null value in one of the parameters
     */
    @Transactional
    public void updatePrices(@NotNull Long productId, @NotNull PricePair pricePair) throws NullPointerException, IllegalArgumentException {
        final Product product;
        final List<PricePair> pricePairList;

        if(productId == null) {
            throw new NullPointerException("ProductId cannot be null");
        }
        if(pricePair == null) {
            throw new NullPointerException("PricePair cannot be null");
        }

        product = productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);

        pricePairList = product.getPricePairs();
        pricePairList.add(pricePair);
        product.setPricePairs(pricePairList);
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

    public List<PricePair> findProductPrices(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get().getPricePairs();
        }
        return new ArrayList<>();
    }

 */
}

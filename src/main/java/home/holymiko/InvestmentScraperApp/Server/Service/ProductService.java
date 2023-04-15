package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.LinkChangeDTO;
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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final PricePairRepository pricePairRepository;

    private final LinkService linkService;

    /////////// FIND AS DTO

    public ProductDTO_LatestPrices findByIdAsDTO(Long id) {
        return productMapper.toProductDTO_LatestPrices(findById(id), pricePairRepository);
    }

    public ProductDTO_AllPrices findByIdAsDTOAllPrices(Long id) {
        return productMapper.toProductDTO_AllPrices(findById(id), pricePairRepository);
    }

    public List<ProductDTO_LatestPrices> findAllAsDTO() {
        return productRepository.findAll()
                .stream()
                .filter(product -> product.getGrams() >= 0)
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

    public List<Product> findByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean isSpecial) {
        return this.productRepository.findByParams(dealer, producer, metal, form, grams, year, isSpecial);
    }

    public List<Product> findByParams(Dealer dealer, ProductCreateDTO product) {
        return this.productRepository.findByParams(dealer, product.getProducer(), product.getMetal(), product.getForm(), product.getGrams(), product.getYear(), product.isSpecial());
    }

    /////////// SAVE

    @Transactional
    public Product save(@NotNull ProductCreateDTO productCreateDTO) throws NullPointerException {
        if(productCreateDTO == null) {
            throw new NullPointerException();
        }
        return this.productRepository.save(
                new Product(productCreateDTO)
        );
    }

    @Transactional
    public Product save(@NotNull Product product) throws NullPointerException {
        if(product == null) {
            throw new NullPointerException();
        }
        return this.productRepository.save(product);
    }

    /**
     * PricePair is added to Product. Product is saved.
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

    @Transactional
    public void changeLinkProduct(LinkChangeDTO dto) {
        Assert.isTrue(!Objects.equals(dto.getToProductId(), dto.getFromProductId()), "fromProductId cannot be same as toProductId");
        Link link = this.linkService.findById(dto.getLinkId());
        Product oldProduct = findById(dto.getFromProductId());
        Assert.isTrue(link.getProductId() == oldProduct.getId(), "Link hasn't reference Product with ID fromProductId");

        if(dto.getToProductId() == null) {
            // Save Link separately
            // TODO Finish impl. of Save Link separately
        } else {
            // Connect Link to existing Product
            Product newProduct = findById(dto.getToProductId());
            Assert.isTrue(link.getProductId() != newProduct.getId(), "Link already has reference to Product with ID toProductId");
            // TODO Finish impl. of Connect Link to existing Product
        }
    }

    /////////// UTILS
    private Product findById(Long id) {
        Optional<Product> optional = this.productRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Product with id "+id+" was not found");
        }
        return optional.get();
    }

}

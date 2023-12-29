package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.LinkChangeDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_Link_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Mapper.ProductMapper;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public Long countByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean saveAlone) {
        return productRepository.countByParams(dealer, producer, metal, form, grams, year, saveAlone);
    }

    /////////// FIND AS DTO

    public ProductDTO_AllPrices findByIdAsDTOAllPrices(Long id) {
        return productMapper.toProductDTO_AllPrices(findById(id), pricePairRepository);
    }

    public ProductDTO_Link_AllPrices findByIdAsDTOLinkAllPrices(Long id) {
        return productMapper.toProductDTO_Link_AllPrices(findById(id), pricePairRepository);
    }

    /////////// FIND

    public List<ProductDTO_LatestPrices> findByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean saveAlone, Pageable pageable) {
        return productRepository.findByParams(dealer, producer, metal, form, grams, year, saveAlone, pageable)
                .stream()
                .map(x -> productMapper.toProductDTO_LatestPrices(x, pricePairRepository))
                .collect(Collectors.toList());
    }

    public List<Product> findByParams(Dealer dealer, ProductCreateDTO product) {
        return this.productRepository.findByParams(dealer, product.getProducer(), product.getMetal(), product.getForm(), product.getGrams(), product.getYear(), product.isSaveAlone(), Pageable.unpaged());
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
     * PricePair is added to Link. Link is saved.
     * @param linkId Link where the PricePair gonna be added
     * @param pricePair PricePair already saved in DB, which should be added to Product
     * @throws NullPointerException For null value in one of the parameters
     */
    @Transactional
    public void updatePrices(@NotNull Long linkId, @NotNull PricePair pricePair) throws NullPointerException, IllegalArgumentException {
        final Link link;
        final List<PricePair> pricePairList;

        if(linkId == null) {
            throw new NullPointerException("LinkId cannot be null");
        }
        if(pricePair == null) {
            throw new NullPointerException("PricePair cannot be null");
        }

        link = linkService.findById(linkId);

        pricePairList = link.getPricePairs();
        pricePairList.add(pricePair);
        link.setPricePairs(pricePairList);
        this.linkService.saveOrUpdate(link);
    }

    @Transactional
    public ProductDTO_Link_AllPrices changeLinkProduct(LinkChangeDTO dto) {
        Product theProduct;
        Assert.isTrue(!Objects.equals(dto.getToProductId(), dto.getFromProductId()), "fromProductId cannot be same as toProductId");
        Link link = this.linkService.findById(dto.getLinkId());
        Product oldProduct = findById(dto.getFromProductId());
        Assert.isTrue(link.getProductId() == oldProduct.getId(), "Link hasn't reference Product with ID fromProductId");

        if(dto.getToProductId() == null) {
            // Save Link separately, create Product copy
            LOGGER.info("Create Product copy and save Link separately");
            theProduct = new Product(
                    link.getName(), oldProduct.getProducer(), oldProduct.getForm(), oldProduct.getMetal(),
                    oldProduct.getGrams(), oldProduct.getYear(), oldProduct.isSaveAlone(), List.of(link)
            );
        } else {
            // Connect Link to existing Product
            LOGGER.info("Connect Link to existing Product");
            Assert.isTrue(link.getProductId() != dto.getToProductId(), "Link already has reference to Product with ID toProductId");
            theProduct = findById( dto.getToProductId() );
            theProduct.getLinks().add(link);
        }
        theProduct = this.productRepository.save( theProduct );
        // Update Link
        link.setProductId( theProduct.getId() );
        this.linkService.saveOrUpdate(link);
        return productMapper.toProductDTO_Link_AllPrices(
                theProduct,
                pricePairRepository
        );
    }

    /////////// UTILS
    private Product findById(Long id) {
        Optional<Product> optional = this.productRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Product with id "+id+" was not found");
        }
        return optional.get();
    }

    @Transactional
    public void deleteById(Long productId) {
        if(productId == null) {
            throw new NullPointerException("ProductId cannot be null");
        }
        productRepository.deleteById(productId);
    }

    @Transactional
    public void deleteAll() {
        productRepository.deleteAll();
    }

}

package home.holymiko.investment.scraper.app.server.service;

import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Extract;
import home.holymiko.investment.scraper.app.server.type.dto.LinkChangeDTO;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_AllPrices;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_LatestPrices;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_Link_AllPrices;
import home.holymiko.investment.scraper.app.server.type.dto.create.ProductCreateDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.type.enums.Producer;
import home.holymiko.investment.scraper.app.server.mapper.ProductMapper;
import home.holymiko.investment.scraper.app.server.api.repository.ProductRepository;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.type.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final LinkService linkService;

    public Long countByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean hidden, Boolean isTopProduct) {
        return productRepository.countByParams(dealer, producer, metal, form, grams, year, hidden, isTopProduct);
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    /////////// FIND AS DTO

    public ProductDTO_AllPrices findByIdAsDTOAllPrices(Long id) {
        return productMapper.toProductDTO_AllPrices(findById(id));
    }

    public ProductDTO_Link_AllPrices findByIdAsDTOLinkAllPrices(Long id) {
        return productMapper.toProductDTO_Link_AllPrices(findById(id));
    }

    /////////// FIND

    public Page<ProductDTO_LatestPrices> findByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean hidden, Boolean isTopProduct, Pageable pageable) {
        Long productCount = productRepository.countByParams(dealer, producer, metal, form, grams, year, hidden, isTopProduct);
        List<ProductDTO_LatestPrices> products =
                productRepository.findByParams(dealer, producer, metal, form, grams, year, hidden, isTopProduct, pageable)
                        .stream()
                        .map(productMapper::toProductDTO_LatestPrices)
                        .collect(Collectors.toList());
        return new PageImpl<>(products, pageable, productCount);
    }

    public List<Product> findByParams(Dealer dealer, ProductCreateDTO product) {
        return this.productRepository.findByParams(
                dealer,
                product.getProducer(),
                product.getMetal(),
                product.getForm(),
                product.getGrams(),
                product.getYear(),
                product.isHidden(),
                product.isTopProduct(),
                Pageable.unpaged()
        ).getContent();
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

    @Transactional
    public ProductDTO_Link_AllPrices updateLinkReference(LinkChangeDTO dto) {
        Product theProduct;
        Assert.isTrue(!Objects.equals(dto.getToProductId(), dto.getFromProductId()), "fromProductId cannot be same as toProductId");
        final Link link = this.linkService.findById(dto.getLinkId());
        final Product oldProduct = findById(dto.getFromProductId());
        Assert.isTrue(link.getProductId() == oldProduct.getId(), "Link hasn't reference Product with ID fromProductId");

        if(dto.getToProductId() == null) {
            // Save Link separately, extract Product from Link name
            LOGGER.info("Extract Product from Link name and save Link separately");
            theProduct = new Product(
                    Extract.productAggregateExtract(link.getName())
            );
        } else {
            // Connect Link to existing Product
            LOGGER.info("Connect Link to existing Product");
            Assert.isTrue(link.getProductId() != dto.getToProductId(), "Link already has reference to Product with ID toProductId");
            theProduct = findById( dto.getToProductId() );
            theProduct.getLinks().add(link);
        }
        // Update Product
        theProduct = this.productRepository.save( theProduct );
        // Update Link
        link.setProductId( theProduct.getId() );
        this.linkService.saveOrUpdate(link);

        if(oldProduct.getLinks().size() == 1) {
            // Remove Product without Link
            this.productRepository.deleteById(dto.getFromProductId());
        }
        return productMapper.toProductDTO_Link_AllPrices(theProduct);
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

}

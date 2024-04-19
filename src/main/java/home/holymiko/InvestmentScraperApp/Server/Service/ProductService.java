package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Extract;
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

    public Long countByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean saveAlone) {
        return productRepository.countByParams(dealer, producer, metal, form, grams, year, saveAlone);
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

    @Deprecated
    public List<ProductDTO_LatestPrices> findByParamsOld(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean saveAlone, Pageable pageable) {
        return productRepository.findByParams(dealer, producer, metal, form, grams, year, saveAlone, pageable).getContent()
                .stream()
                .map(productMapper::toProductDTO_LatestPrices)
                .collect(Collectors.toList());
    }

    public Page<ProductDTO_LatestPrices> findByParams(Dealer dealer, Producer producer, Metal metal, Form form, Double grams, Integer year, Boolean saveAlone, Pageable pageable) {
        List<ProductDTO_LatestPrices> products =
                productRepository.findByParams(dealer, producer, metal, form, grams, year, saveAlone, pageable)
                        .stream()
                        .map(productMapper::toProductDTO_LatestPrices)
                        .collect(Collectors.toList());
        return new PageImpl<>(products, pageable, products.size());
    }

    public List<Product> findByParams(Dealer dealer, ProductCreateDTO product) {
        return this.productRepository.findByParams(dealer, product.getProducer(), product.getMetal(), product.getForm(), product.getGrams(), product.getYear(), product.isSaveAlone(), Pageable.unpaged()).getContent();
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

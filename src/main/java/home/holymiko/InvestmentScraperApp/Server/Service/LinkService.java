package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Mapper.LinkMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.LinkRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LinkService {
    private final LinkMapper linkMapper;
    private final LinkRepository linkRepository;
    private final ProductRepository productRepository;

    public Long countByParams(Dealer dealer)  {
        return this.linkRepository.countByParams(dealer);
    }

    /**
     * @throws NullPointerException input validation
     * @throws ResourceNotFoundException occurrence validation
     */
    public Link findById(@NotNull Long linkId) {
        if(linkId == null) {
            throw new NullPointerException("LinkId cannot be null");
        }
        Optional<Link> optional = this.linkRepository.findById(linkId);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Link with id "+linkId+" was not found");
        }
        return optional.get();
    }

    public LinkDTO findByIdAsDto(Long linkId) throws IllegalArgumentException {
        return linkMapper.toDTO(
                linkRepository.findById(linkId).orElseThrow(IllegalArgumentException::new)
        );
    }

    public List<List<LinkDTO>> findLinksGroupedByProduct() {
        return this.productRepository.findAll().stream().map(
                    product -> linkMapper.toDTO(product.getLinks())
                ).collect(Collectors.toList());
    }

    public List<List<LinkDTO>> findLinksGroupedByProduct(Metal metal) {
        return this.productRepository.findByParams(
                null, null, metal, null, null, null, null, Pageable.unpaged()
                ).stream().map(
                    product -> linkMapper.toDTO(product.getLinks())
                ).collect(Collectors.toList());
    }

    public List<LinkDTO> findByParams(Long productId, Dealer dealer) {
        return linkMapper.toDTO(
                linkRepository.findByParams(productId, dealer)
        );
    }

    public List<LinkDTO> findByProductId(@Nullable Long product) {
        return linkMapper.toDTO(
                linkRepository.findByProductId(product)
        );
    }

    /**
     * Link's productId is set. Link is saved
     * Product links are extended, but productId is not saved here.
     * @param linkId
     * @param productId
     * @throws NullPointerException linkId or productId is null
     */
    public LinkDTO updateLinkProductId(@NotNull Long linkId, @NotNull Long productId, String productName) throws NullPointerException, IllegalArgumentException {
        if(productId == null) {
            throw new NullPointerException("ProductId cannot be null");
        }
        if(linkId == null) {
            throw new NullPointerException("LinkId cannot be null");
        }

        final Link link = findById(linkId);
        link.setProductId(productId);
        link.setName(productName);      // TODO Fill this during scrapLink from ProductListPage
        return linkMapper.toDTO(
                linkRepository.save(link)
        );
    }

    /**
     * Saves Link to DB
     * Prevents duplicities of URL. Every Link has to have unique URL.
     * @param link to be saved
     * @throws DataIntegrityViolationException link.url already present in DB || link.url has duplicities in DB
     * @throws NullPointerException if parameter link == null
     */
    @Transactional
    public void saveOrUpdate(@NotNull Link link) throws DataIntegrityViolationException, IllegalArgumentException, NullPointerException {
        // Validate inputs
        if(link == null) {
            throw new NullPointerException("Save - Link cannot be null");
        }
        uriFilter( link.getUri() );

        Optional<Link> optional = linkRepository.findByUri(link.getUri());
        // Switches save & update
        if ( optional.isPresent() && !Objects.equals(optional.get().getId(), link.getId())) {
            // Same URI, different ID
            throw new DataIntegrityViolationException("Link already in DB");
        }
        linkRepository.save(link);
    }

    /**
     * Filtration based on text of the uri
     * @param uri Link about to be filtered
     * @throws IllegalArgumentException for uri being filtered
     */
    private static void uriFilter(String uri) throws IllegalArgumentException {
        if (uri.contains("etuje") || uri.contains("etui")) {
            throw new IllegalArgumentException("uri vyřazena: etuje - " + uri);
        }
        if (uri.contains("obalka")) {
            throw new IllegalArgumentException("uri vyřazena: obalka - " + uri);
        }
        if (uri.contains("kapsle")) {
            throw new IllegalArgumentException("uri vyřazena: kapsle - " + uri);
        }
    }

}

package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
import home.holymiko.InvestmentScraperApp.Server.Mapper.LinkMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.LinkRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final LinkMapper linkMapper;
    private final ProductRepository productRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository, LinkMapper linkMapper, ProductRepository productRepository) {
        this.linkRepository = linkRepository;
        this.linkMapper = linkMapper;
        this.productRepository = productRepository;
    }

    public LinkDTO findById(Long linkId) throws IllegalArgumentException {
        return linkMapper.toDTO(
                linkRepository.findById(linkId).orElseThrow(IllegalArgumentException::new)
        );
    }

    public LinkDTO findByDealerAndProductId(Dealer dealer, long productId) {
        return linkMapper.toDTO(
                linkRepository.findByDealerAndProduct_Id(dealer, productId).orElseThrow(IllegalArgumentException::new)
        );
    }

    public List<List<LinkDTO>> findLinksGroupedByProduct() {
        return this.productRepository.findAll().stream().map(
                product -> linkMapper.toDTO(product.getLinks())
        ).collect(Collectors.toList());
    }

    public List<List<LinkDTO>> findLinksGroupedByProduct(Metal metal) {
        return this.productRepository.findProductsByMetal(metal).stream().map(
                product -> linkMapper.toDTO(product.getLinks())
        ).collect(Collectors.toList());
    }

    public List<LinkDTO> findByProductParams(Metal metal, Form form) {
        return linkMapper.toDTO(
                linkRepository.findByProductParams(metal, form)
        );
    }

    public List<LinkDTO> findAll() {
        return linkMapper.toDTO(
                this.linkRepository.findAll()
        );
    }

    public List<LinkDTO> findByDealer(Dealer dealer) {
        return linkMapper.toDTO(
                linkRepository.findByDealer(dealer)
        );
    }

    public List<LinkDTO> findByProductId(@Nullable Long product) {
        return linkMapper.toDTO(
                linkRepository.findByProduct_Id(product)
        );
    }

    /**
     * Link's product is set. Link is saved
     * Product links are extended, but product is not saved here.
     * @param linkId
     * @param product
     * @return
     * @throws NullPointerException
     */
    public LinkDTO updateProductLinks(@NotNull Long linkId, @NotNull Product product) throws NullPointerException, IllegalArgumentException {
        final Link link;
        final Optional<Link> optionalLink = linkRepository.findById(linkId);

        if(optionalLink.isEmpty()) {
            throw new IllegalArgumentException("Link with given ID doesnt exist");
        }
        if(product == null) {
            throw new NullPointerException("Product cannot be null");
        }
        link = optionalLink.get();

        product.getLinks().add(link);
        link.setProduct(product);
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
    public void save(@NotNull Link link) throws DataIntegrityViolationException, IllegalArgumentException, NullPointerException {
        if(link == null) {
            throw new NullPointerException("Save - Link cannot be null");
        }

        linkFilter( link.getUrl() );

        if ( linkRepository.findByUrl(link.getUrl()).isPresent() ) {
            throw new DataIntegrityViolationException("Link already in DB");
        }
        linkRepository.save(link);
    }

    /**
     * Filtration based on text of the link
     * @param link Link about to be filtered
     * @return False for link being filtered
     */
    private static void linkFilter(String link) throws IllegalArgumentException {
        if (link.contains("etuje") || link.contains("etui")) {
            throw new IllegalArgumentException("Link vyřazen: etuje - " + link);
        }
        if (link.contains("obalka")) {
            throw new IllegalArgumentException("Link vyřazen: obalka - " + link);
        }
        if (link.contains("kapsle")) {
            throw new IllegalArgumentException("Link vyřazen: kapsle - " + link);
        }
    }

}

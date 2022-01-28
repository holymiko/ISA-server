package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.LinkRepository;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> findAll() {
        return this.linkRepository.findAll();
    }

    public Link findById(Long linkId) throws IllegalArgumentException {
        return linkRepository.findById(linkId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Link> findByParams(Dealer dealer, Metal metal, Form form, String url) {
        return linkRepository.findByParams(dealer, metal, form, url);
    }

    public Optional<Link> findByDealerAndProductId(Dealer dealer, long product) {
        return linkRepository.findByDealerAndProduct_Id(dealer, product);
    }

    public List<Link> findByProductId(long product) {
        return linkRepository.findByProduct_Id(product);
    }

    public Link updateProduct(@NotNull Long linkId, Product product) throws NullPointerException {
        final Link link;
        final Optional<Link> optionalLink = linkRepository.findById(linkId);

        if(optionalLink.isEmpty()) {
            throw new NullPointerException("Link cannot be null");
        }
        link = optionalLink.get();

        product.getLinks().add(link);
        link.setProduct(product);
        return linkRepository.save(link);
    }

    /**
     * Saves Link to DB
     * Prevents duplicities of URL. Every Link has to have unique URL.
     * @param link to be saved
     * @throws DataIntegrityViolationException link.url already present in DB || link.url has duplicities in DB
     * @throws NullPointerException if parameter link == null
     */
    public void save(@NotNull Link link) throws DataIntegrityViolationException, NullPointerException {
        if(link == null) {
            throw new NullPointerException("Save - Link cannot be null");
        }

        if ( linkRepository.findByUrl(link.getUrl()).isPresent() ) {
            throw new DataIntegrityViolationException("Link already in DB");
        }
        linkRepository.save(link);
    }

}

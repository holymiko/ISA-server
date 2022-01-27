package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.LinkRepository;
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

    public List<Link> findByLink(String link) {
        return linkRepository.findByUrl(link);
    }

//    public List<Link> findByMetal(Metal metal) {
//        return linkRepository.findByMetal(metal);
//    }

    public List<Link> findByDealer(Dealer dealer) {
        return linkRepository.findByDealer(dealer);
    }

    public Optional<Link> findByDealerAndProductId(Dealer dealer, long product) {
        return linkRepository.findByDealerAndProduct_Id(dealer, product);
    }

//    public List<Link> findByProducer(Producer producer) {
//        return linkRepository.findByProducer(producer);
//    }

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
            throw new NullPointerException("Link cannot be null");
        }

        switch ( linkRepository.findByUrl(link.getUrl()).size() ) {
            case 0 -> linkRepository.save(link);
            case 1 -> throw new DataIntegrityViolationException("Link already in DB");
            default -> throw new DataIntegrityViolationException("WARNING - Duplicates in DB table LINK");
        }
    }

}

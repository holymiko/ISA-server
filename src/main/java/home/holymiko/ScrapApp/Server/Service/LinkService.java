package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.Link;
import home.holymiko.ScrapApp.Server.Repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Link> findByLink(String link) {
        return linkRepository.findByLink(link);
    }

//    public List<Link> findByMetal(Metal metal) {
//        return linkRepository.findByMetal(metal);
//    }

    public List<Link> findByDealer(Dealer dealer) {
        return linkRepository.findByDealer(dealer);
    }

//    public List<Link> findByProducer(Producer producer) {
//        return linkRepository.findByProducer(producer);
//    }

    public void save(Link link) {
        linkRepository.save(link);
    }

}

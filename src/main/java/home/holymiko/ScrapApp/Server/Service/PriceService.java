package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.Entity.Price;
import home.holymiko.ScrapApp.Server.Repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public void save(Price price) {
        this.priceRepository.save(price);
    }
}

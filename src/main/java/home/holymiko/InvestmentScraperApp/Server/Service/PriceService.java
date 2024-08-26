package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.LinkRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairHistoryRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PriceRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePairHistory;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Availability;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PriceService {
    private final PricePairRepository pricePairRepository;
    private final PricePairHistoryRepository pricePairHistoryRepository;
    private final PriceRepository priceRepository;
    private final LinkRepository linkRepository;
    private final LinkService linkService;

    public Long countPricePairs()  {
        return this.pricePairRepository.count();
    }

    public Long countPricePairsHistory()  {
        return this.pricePairHistoryRepository.count();
    }

    /**
     * Saves Price, PricePair and PricePairHistory to DB.
     * Link references are updated.
     * @param linkId Link to which the PricePair and PricePairHistory is added
     * @param buy parameter of Price
     * @param sell parameter of Price
     * @param saveHistory switch to save PricePairHistory
     * @throws NullPointerException linkId is null
     * @throws ResourceNotFoundException Link for linkId doesn't exist
     */
    @Transactional
    public void savePriceAndUpdateLink(@NotNull Long linkId, Double buy, Double sell, Availability availability, String availabilityMsg, boolean saveHistory) {
        final Link link;
        final Price buyPrice;
        final Price sellPrice;
        final PricePair pricePair;
        final PricePairHistory pricePairHistory;
        final List<PricePairHistory> pricePairList;

        // Validate inputs
        link = this.linkService.findById(linkId);
        pricePairList = link.getPricePairsHistory();

        // Remove old PricePair
        if(link.getPricePair() != null) {
            this.pricePairRepository.delete(link.getPricePair());
        }

        // Save new Prices
        buyPrice = this.priceRepository.save(new Price(LocalDateTime.now(), buy, false));
        sellPrice = this.priceRepository.save(new Price(LocalDateTime.now(), sell, true));

        // Save new PricePair
        pricePair = new PricePair(buyPrice, sellPrice, availability, availabilityMsg);
        this.pricePairRepository.save(pricePair);
        // Update relations
        link.setPricePair(pricePair);

        if(saveHistory) {
            // Save new PricePair History
            pricePairHistory = new PricePairHistory(buyPrice, sellPrice, availability, linkId);
            this.pricePairHistoryRepository.save(pricePairHistory);
            // Update relations
            pricePairList.add(pricePairHistory);
            link.setPricePairsHistory(pricePairList);
        }
        // Save/Update Link
        linkRepository.save(link);
    }

}

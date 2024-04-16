package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.LinkRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairHistoryRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PriceRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePairHistory;
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
     * @param sell parameter of Price
     * @param buyOut parameter of Price
     * @throws NullPointerException linkId is null
     * @throws ResourceNotFoundException Link for linkId doesn't exist
     */
    @Transactional
    public void savePriceAndUpdateLink(@NotNull Long linkId, Double sell, Double buyOut) {
        final Link link;
        final Price sellPrice;
        final Price buyPrice;
        final PricePair pricePair;
        final PricePairHistory pricePairHistory;
        final List<PricePairHistory> pricePairList;

        // Validate inputs
        link = this.linkService.findById(linkId);

        // Remove old record
        if(link.getPricePair() != null) {
            this.pricePairRepository.delete(link.getPricePair());
        }

        // Save new records
        sellPrice = this.priceRepository.save(new Price(LocalDateTime.now(), sell, false));
        buyPrice = this.priceRepository.save(new Price(LocalDateTime.now(), buyOut, true));
        pricePair = new PricePair(sellPrice, buyPrice);
        pricePairHistory = new PricePairHistory(sellPrice, buyPrice, linkId);
        this.pricePairRepository.save(pricePair);
        this.pricePairHistoryRepository.save(pricePairHistory);

        // Update relations
        pricePairList = link.getPricePairsHistory();
        pricePairList.add(pricePairHistory);
        link.setPricePair(pricePair);
        link.setPricePairsHistory(pricePairList);
        linkRepository.save(link);
    }

}

package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PriceRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService {
    private final PricePairRepository pricePairRepository;
    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PricePairRepository pricePairRepository, PriceRepository priceRepository) {
        this.pricePairRepository = pricePairRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional
    public void updatePricePair(@NotNull String productName, @NotNull Dealer dealer, @NotNull Double amount, boolean isRedemption) throws NullPointerException, IllegalArgumentException {
        final PricePair pricePair;

        if(productName == null) {
            throw new NullPointerException("productName cannot be null");
        }
        if(dealer == null) {
            throw new NullPointerException("dealer cannot be null");
        }
        if(amount == null) {
            throw new NullPointerException("amount cannot be null");
        }

        pricePair = pricePairRepository.findByProduct_NameAndDealer(productName, dealer)
                .orElseThrow(IllegalArgumentException::new);

        if(isRedemption) {
            pricePair.setRedemption(new Price(LocalDateTime.now(), amount, true));
        } else {
            pricePair.setSellPrice(new Price(LocalDateTime.now(), amount, false));
        }

        this.pricePairRepository.save(pricePair);
    }

    /**
     * Method used for resolving issue of Product.latestPrices vs. Product.prices
     * @param productId
     * @return Latest PricePair for each Dealer
     */
    public List<PricePair> findLatestPricePairsByProductId(@NotNull Long productId) throws NullPointerException {
        if(productId == null) {
            throw new NullPointerException("Product ID can't be null");
        }
        // TODO test invalid productId & add productId Validation
        return pricePairRepository.findLatestPricePairsByProductId(productId);
    }

    public PricePair getPriceByBestRedemption(Long productId) {
        List<PricePair> pricePairs = findLatestPricePairsByProductId(productId);
        PricePair max = pricePairs.get(0);
        for (PricePair pricePair : pricePairs) {
            if(pricePair.getRedemption().getAmount() > max.getRedemption().getAmount()) {
                max = pricePair;
            }
        }
        return max;
    }

    @Transactional
    public PricePair save(PricePair pricePair) {
        return this.pricePairRepository.save(pricePair);
    }

    @Transactional
    public Price save(Price price) {
        return this.priceRepository.save(price);
    }
}

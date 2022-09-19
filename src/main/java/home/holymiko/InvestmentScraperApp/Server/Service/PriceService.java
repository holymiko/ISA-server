package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PriceRepository;
import home.holymiko.InvestmentScraperApp.Server.Mapper.PricePairMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PricePairDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceService {
    private final PricePairRepository pricePairRepository;
    private final PriceRepository priceRepository;
    private final PricePairMapper pricePairMapper;

    @Autowired
    public PriceService(PricePairRepository pricePairRepository, PriceRepository priceRepository, PricePairMapper pricePairMapper) {
        this.pricePairRepository = pricePairRepository;
        this.priceRepository = priceRepository;
        this.pricePairMapper = pricePairMapper;
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
    public List<PricePairDTO> findLatestPricePairsByProductId(@NotNull Long productId) throws NullPointerException {
        if(productId == null) {
            throw new NullPointerException("Product ID can't be null");
        }
        // TODO test invalid productId & add productId Validation
        return pricePairMapper.toPriceDTOs(
                pricePairRepository.findLatestPricePairsByProductId(productId)
        );
    }

    public PricePairDTO getPriceByBestRedemption(Long productId) {
        List<PricePairDTO> pricePairs = findLatestPricePairsByProductId(productId);
        PricePairDTO max = pricePairs.get(0);
        for (PricePairDTO pricePair : pricePairs) {
            if(pricePair.getRedemption() > max.getRedemption()) {
                max = pricePair;
            }
        }
        return max;
    }

    @Transactional
    public PricePairDTO save(PricePair pricePair) {
        return pricePairMapper.toPriceDTO(
                this.pricePairRepository.save(pricePair)
        );
    }

    @Transactional
    public Price save(Price price) {
        return this.priceRepository.save(price);
    }
}

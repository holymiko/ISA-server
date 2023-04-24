package home.holymiko.InvestmentScraperApp.Server.Service;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PriceRepository;
import home.holymiko.InvestmentScraperApp.Server.Mapper.PricePairMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PricePairDTO_Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PriceService {
    private final PricePairRepository pricePairRepository;
    private final PriceRepository priceRepository;
    private final PricePairMapper pricePairMapper;

    @Deprecated
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

//        pricePair = pricePairRepository.findByProduct_NameAndDealer(productName, dealer)
//                .orElseThrow(IllegalArgumentException::new);
//
//        pricePair.setRedemption(new Price(LocalDateTime.now(), amount, isRedemption));
//
//        this.pricePairRepository.save(pricePair);
    }

    /**
     * Method used for resolving issue of Product.latestPrices vs. Product.prices
     * @param productId
     * @return Latest PricePair for each Dealer
     */
    @Deprecated
    public List<PricePairDTO_Dealer> findLatestPricePairsByProductId(@NotNull Long productId) throws NullPointerException {
        if(productId == null) {
            throw new NullPointerException("Product ID can't be null");
        }
//        // TODO ProductRepo getProductById
//        // TODO test invalid productId & add productId Validation
//        return pricePairMapper.toPriceDTOs(
//                pricePairRepository.findLatestPricePairsByProductId(productId)
//        );
        return new ArrayList<>();
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

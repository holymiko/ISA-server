package home.holymiko.investment.scraper.app.server.api.controller;

import home.holymiko.investment.scraper.app.server.service.LinkService;
import home.holymiko.investment.scraper.app.server.service.ProductService;
import home.holymiko.investment.scraper.app.server.type.dto.simple.LinkCountDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.LinkDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v2/link")
@AllArgsConstructor
public class LinkController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final LinkService linkService;
    private final ProductService productService;

    @GetMapping
    public List<LinkDTO> findByParams(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Dealer dealer
    ) {
        LOGGER.info("findByParams {} {}", productId, dealer);
        return this.linkService.findByParams(productId, dealer);
    }

    @GetMapping("/count")
    public List<LinkCountDTO> countLinks() {
        LOGGER.info("countLinks");

        return new ArrayList<>(
            Arrays.stream(Dealer.values()).map((dealer) ->
                new LinkCountDTO(
                    dealer,
                    linkService.countByParams(dealer, true),
                    linkService.countByParams(dealer, false),
                    productService.countByParams(dealer, null, null, null, null, null, true, null)
                )
            ).toList()
        );
    }

}

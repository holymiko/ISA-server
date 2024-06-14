package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkCountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
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

    @GetMapping
    public List<LinkDTO> findByParams(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Dealer dealer
    ) {
        LOGGER.info("findByParams {} {}", productId, dealer);
        return this.linkService.findByParams(productId, dealer);
    }

    @GetMapping("/count")
    public List<LinkCountDTO> countByParams(
            @RequestParam(required = false) Dealer dealer
    ) {
        LOGGER.info("countByParams {}", dealer);

        return new ArrayList<>(
            Arrays.stream(Dealer.values()).map((d) ->
                new LinkCountDTO(
                    d,
                    linkService.countByParams(d),
                    linkService.countByParams(d, true)
                )
            ).toList()
        );
    }

}

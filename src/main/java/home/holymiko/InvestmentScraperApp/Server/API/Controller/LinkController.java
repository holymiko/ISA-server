package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/link")
@AllArgsConstructor
public class LinkController extends BaseController {

    private final LinkService linkService;

    @GetMapping
    public List<LinkDTO> findByParams(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Dealer dealer) {
        return this.linkService.findByParams(productId, dealer);
    }

    // TODO getCount
}

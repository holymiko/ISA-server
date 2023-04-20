package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/link")
@AllArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping
    public List<LinkDTO> findByDealer(@RequestParam Dealer dealer) {
        return this.linkService.findByDealer(dealer);
    }

    // TODO getCount
}

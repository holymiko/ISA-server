package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")         // Requesty z teto adresy jsou legit
@RestController
@RequestMapping("/api/v2/portfolio")         // Na url/api/v1/portfolio se zavola HTTP request
@AllArgsConstructor
public class PortfolioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioController.class);

    private final PortfolioService portfolioService;

    /////// GET

    @GetMapping
    public List<PortfolioDTO> all() {
        LOGGER.info("Get all portfolios");
        return portfolioService.findAllAsDTO();
    }

    @GetMapping("/{id}")
    public PortfolioDTO_ProductDTO byId(@PathVariable Long id) {
        LOGGER.info("Get by Id");
        return portfolioService.findByIdAsDTO(id);
    }


//    /////// GET DTO
//
//    @GetMapping({"/dto/{dtoSwitch}","/dto/{dtoSwitch}/"})
//    public List<PortfolioDTO> asDTO(@PathVariable int dtoSwitch) {
//        LOGGER.info("Get all portfolios as DTO");
//        return portfolioService.findAllAsPortfolioDTO(dtoSwitch);
//    }
//
//    @ResourceNotFound
//    @GetMapping("/dto/{dtoSwitch}/id/{id}")
//    public Optional<PortfolioDTO> byIdAsDTO(
//            @PathVariable("dtoSwitch") int dtoSwitch,
//            @PathVariable("id") long id
//    ) {
//        LOGGER.info("Get by Id as Portfolio-InvestmentMetal DTO");
//        return portfolioService.findByIdAsPortfolioDTO(id, dtoSwitch);
//    }
//
//
//    /////// POST
//
//    @PostMapping({"/", ""})
//    public void createPortfolio(@RequestBody PortfolioCreateDTO portfolioCreateDTO) {
//        LOGGER.info("Add Portfolio");
////        this.portfolioService.save(portfolioCreateDTO);
//    }

}

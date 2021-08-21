package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.create.PortfolioCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")         // Requesty z teto adresy jsou legit
@RestController
@RequestMapping("/api/v2/portfolio")         // Na url/api/v1/portfolio se zavola HTTP request
public class PortfolioController {
    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
//        start();
    }

    private void start() {
        portfolioService.saveInitPortfolios();
//        this.portfolioService.addInvestmentToPortfolio("Carlos",
//                this.investmentService.save(
//                    new InvestmentMetal (
//                            this.productService.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(
//                                    Dealer.BESSERGOLD,
//                                    Producer.MUNZE_OSTERREICH,
//                                    Metal.GOLD,
//                                    Form.KINEBAR,
//                                    10
//                            ), 14569.00, LocalDate.of(2021, 5, 18))));

    }

    /////// GET

    @GetMapping({"/", ""})
    public List<PortfolioDTO> all() {
        System.out.println("Get all portfolios");
        return portfolioService.findAllAsDTO();
    }

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<PortfolioDTO_ProductDTO> byId(@PathVariable long id) {
        System.out.println("Get by Id");
        return portfolioService.findById(id);
    }

    @ResourceNotFound
    @GetMapping("/owner/{owner}")
    public Optional<PortfolioDTO_ProductDTO> byOwner(@PathVariable String owner) {
        return portfolioService.findByOwner(owner);
    }


    /////// GET DTO

    @GetMapping({"/dto/{dtoSwitch}","/dto/{dtoSwitch}/"})
    public List<PortfolioDTO> asDTO(@PathVariable int dtoSwitch) {
        System.out.println("Get all portfolios as DTO");
        return portfolioService.findAllAsPortfolioDTO(dtoSwitch);
    }

    @ResourceNotFound
    @GetMapping("/dto/{dtoSwitch}/id/{id}")
    public Optional<PortfolioDTO> byIdAsDTO(
            @PathVariable("dtoSwitch") int dtoSwitch,
            @PathVariable("id") long id
    ) {
        System.out.println("Get by Id as Portfolio-InvestmentMetal DTO");
        return portfolioService.findByIdAsPortfolioDTO(id, dtoSwitch);
    }


    /////// POST

    @PostMapping({"/", ""})
    public void createPortfolio(@RequestBody PortfolioCreateDTO portfolioCreateDTO) {
        System.out.println("Add Portfolio");
//        this.portfolioService.save(portfolioCreateDTO);
    }

}

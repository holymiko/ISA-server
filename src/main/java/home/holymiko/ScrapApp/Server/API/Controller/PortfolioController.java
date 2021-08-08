package home.holymiko.ScrapApp.Server.API.Controller;

import home.holymiko.ScrapApp.Server.DTO.create.PortfolioCreateDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<Portfolio> all() {
        System.out.println("Get all portfolios");
        return portfolioService.findAll();
    }

    @GetMapping("/id/{id}")
    public Portfolio byId(@PathVariable long id) {
        System.out.println("Get by Id");
        Optional<Portfolio> optionalPortfolio = portfolioService.findById(id);
        if (optionalPortfolio.isPresent()) {
            return optionalPortfolio.get();
        } else {
            String tryOwner = "Mikolas";
            optionalPortfolio = portfolioService.findByOwner(tryOwner);
            if (optionalPortfolio.isPresent())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Try: " + byOwner(tryOwner).getId());
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id: " + id + " not found.\nTried byOwner: " + tryOwner + " - didn't work");
        }
    }

    @GetMapping("/owner/{owner}")
    public Portfolio byOwner(@PathVariable String owner) {
        return portfolioService.findByOwner(owner).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Try owner Mikolas"));
    }


    /////// GET DTO

    @GetMapping({"/dto/{dtoSwitch}","/dto/{dtoSwitch}/"})
    public List<PortfolioDTO> asDTO(@PathVariable int dtoSwitch) {
        System.out.println("Get all portfolios as DTO");
        return portfolioService.findAllAsPortfolioDTO(dtoSwitch);
    }

    @GetMapping("/dto/{dtoSwitch}/id/{id}")
    public PortfolioDTO byIdAsDTO(
            @PathVariable("dtoSwitch") int dtoSwitch,
            @PathVariable("id") long id
    ) {
        System.out.println("Get by Id as Portfolio-InvestmentMetal DTO");

        Optional<PortfolioDTO> optionalPortfolio = portfolioService.findByIdAsPortfolioDTO(id, dtoSwitch);
        if (optionalPortfolio.isPresent()) {
            return optionalPortfolio.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    /////// POST

    @PostMapping({"/", ""})
    public void createPortfolio(@RequestBody PortfolioCreateDTO portfolioCreateDTO) {
        System.out.println("Add Portfolio");
//        this.portfolioService.save(portfolioCreateDTO);
    }

}

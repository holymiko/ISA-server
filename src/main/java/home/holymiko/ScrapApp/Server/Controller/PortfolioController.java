package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.DTO.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.Portfolio_Investment_DTO;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Service.InvestmentService;
import home.holymiko.ScrapApp.Server.Service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")         // Requesty z teto adresy jsou legit
@RestController
@RequestMapping("/api/v1/portfolio")         // Na url/api/v1/portfolio se zavola HTTP request
public class PortfolioController {
    private final ScrapController scrapController;
    private final PortfolioService portfolioService;
    private final InvestmentService investmentService;

    @Autowired
    public PortfolioController(ScrapController scrapController, PortfolioService portfolioService, InvestmentService investmentService) {
        this.scrapController = scrapController;
        this.portfolioService = portfolioService;
        this.investmentService = investmentService;
    }

    /////////////// GET

    @GetMapping({"/", ""})
    public List<Portfolio> all() {
        System.out.println("Get all portfolios");
        return portfolioService.findAll();
    }

    @GetMapping({"/dto/portfolio","/dto/portfolio/", "/dto/", "/dto"})
    public List<PortfolioDTO> allAsDTO() {
        System.out.println("Get all portfolios as DTO");
        return portfolioService.findAllAsPortfolioDTO();
    }

    @GetMapping({"/dto/portfolio-investments", "/dto/portfolio-investments/" })
    public List<Portfolio_Investment_DTO> allAsPortfolioInvestmentDTO() {
        System.out.println("Get all portfolios as Portfolio-Investment DTO");
        return portfolioService.findAllAsPortfolioInvestmentDTO();
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

    @GetMapping("/dto/portfolio-investments/id/{id}")
    public Portfolio_Investment_DTO byIdAsPortfolioInvestmentDTO(@PathVariable long id) {
        System.out.println("Get by Id as Portfolio-Investment DTO");

        Optional<Portfolio_Investment_DTO> optionalPortfolio = portfolioService.findByIdAsPortfolioInvestmentDTO(id);
        if (optionalPortfolio.isPresent()) {
            return optionalPortfolio.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/owner/{owner}")
    public Portfolio byOwner(@PathVariable String owner) {
        return portfolioService.findByOwner(owner).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Try owner Mikolas"));
    }


    /////////////// PUT


    @RequestMapping("/scrap/{id}")
    public void updatePortfolioProducts(@PathVariable long id) {
        Optional<Portfolio> optionalPortfolio = portfolioService.findById(id);
        if (optionalPortfolio.isPresent()) {
            this.scrapController.scrapPortfolioProducts(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

//    @PostMapping("/owner/{owner}")
//    public void updatePortfolioProducts(@PathVariable String owner){
//        this.scrap.scrapPortfolioProducts(portfolioService.findByOwner(owner).get().getId());
//    }

    public void saveMyPortfolio() {
        portfolioService.save(new Portfolio(investmentService.saveMyInvestments(), "Mikolas"));
        portfolioService.update(portfolioService.findByOwner("Mikolas").get().getId());
    }

    public void printProducts(List<Product> products) {
        if (!products.isEmpty()) {
            System.out.println(">>>>>>> Products <<<<<<<");
            System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Price", "Price / Gram", "Split");
            System.out.println( "----------------------------------------------------------------------------");
            for (Product product : products) {
                System.out.printf("          %-90s%-10.2f   %10.2f    %-15.2f  %-10f\n",
                        product.getName(),
                        product.getGrams(),
                        product.getLatestPrice().getPrice(),
                        product.getLatestPrice().getPricePerGram(),
                        product.getLatestPrice().getSplit()
                );
            }
        } else
            System.out.println("No products here");
    }

    public void printPortfolio(Portfolio portfolio) {
        List<Investment> investmentList = portfolio.getInvestments();
        System.out.println(">>>>>>> PortfolioDTO <<<<<<<<\n");
        System.out.println( "Owner: " + portfolio.getOwner()+"\n");
        System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Nakup", "Vykup", "Zhodnoceni");
        System.out.println( "----------------------------------------------------------------------------\n");
        for (Investment investment : investmentList) {
            System.out.printf( "%1$-120s", investment.getProduct().getName());
            System.out.printf( "%-15.2f%-10.2f%-15s%-10s\n",
                    investment.getProduct().getGrams(),
                    investment.getBeginPrice(),
                    investment.getProduct().getLatestPrice().getRedemption(),
                    investment.getTextYield()
            );
        }
        System.out.println( "----------------------------------------------------------------------------\n");
        System.out.printf( "%103.0f%10.0f%13s\n", portfolio.getBeginPrice(), portfolio.getValue(), portfolio.getTextYield());
    }

}

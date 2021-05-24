package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.DTO.PortfolioCreateDTO;
import home.holymiko.ScrapApp.Server.DTO.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.Portfolio_Investment_DTO;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Service.InvestmentService;
import home.holymiko.ScrapApp.Server.Service.PortfolioService;
import home.holymiko.ScrapApp.Server.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")         // Requesty z teto adresy jsou legit
@RestController
@RequestMapping("/api/v1/portfolio")         // Na url/api/v1/portfolio se zavola HTTP request
public class PortfolioController {
    private final ScrapController scrapController;
    private final PortfolioService portfolioService;
    private final InvestmentService investmentService;
    private final ProductService productService;

    @Autowired
    public PortfolioController(ScrapController scrapController, PortfolioService portfolioService, InvestmentService investmentService, ProductService productService) {
        this.scrapController = scrapController;
        this.portfolioService = portfolioService;
        this.investmentService = investmentService;
        this.productService = productService;

    }

    private void start() {
//        this.portfolioService.addInvestmentToPortfolio("Mikolas",
//                this.investmentService.save(
//                    new Investment (
//                            this.productService.findProductByLink_DealerAndProducerAndMetalAndFormAndGrams(
//                                    Dealer.BESSERGOLD,
//                                    Producer.MUNZE_OSTERREICH,
//                                    Metal.GOLD,
//                                    Form.BAR,
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

    @GetMapping({"/dto/portfolio","/dto/portfolio/", "/dto/", "/dto"})
    public List<PortfolioDTO> asDTO() {
        System.out.println("Get all portfolios as DTO");
        return portfolioService.findAllAsPortfolioDTO();
    }

    @GetMapping({"/dto/portfolio-investments", "/dto/portfolio-investments/" })
    public List<Portfolio_Investment_DTO> asPortfolio_Investment_DTO() {
        System.out.println("Get all portfolios as Portfolio-Investment DTO");
        return portfolioService.findAllAsPortfolioInvestmentDTO();
    }

    @GetMapping("/dto/portfolio-investments/id/{id}")
    public Portfolio_Investment_DTO byIdAsPortfolio_Investment_DTO(@PathVariable long id) {
        System.out.println("Get by Id as Portfolio-Investment DTO");

        Optional<Portfolio_Investment_DTO> optionalPortfolio = portfolioService.findByIdAsPortfolioInvestmentDTO(id);
        if (optionalPortfolio.isPresent()) {
            return optionalPortfolio.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    /////// POST

    @PostMapping({"/", ""})
    public void createPortfolio(@RequestBody PortfolioCreateDTO portfolioCreateDTO) {
        System.out.println("Add Portfolio");
        this.portfolioService.save(portfolioCreateDTO);
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

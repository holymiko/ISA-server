package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.InvestmentDTO;
import home.holymiko.ScrapApp.Server.DTO.PortfolioCreateDTO;
import home.holymiko.ScrapApp.Server.DTO.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.Portfolio_Investment_DTO;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    private final ProductService productService;
    private final InvestmentService investmentService;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, ProductService productService, InvestmentService investmentService) {
        this.portfolioRepository = portfolioRepository;
        this.productService = productService;
        this.investmentService = investmentService;
    }

    /////// to DTO

    private PortfolioDTO toPortfolioDTO(Portfolio portfolio) {
        return new PortfolioDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                portfolio.getBeginPrice(),
                portfolio.getValue(),
                portfolio.getYield(),
                portfolio.getInvestments().stream().map(Investment::getId).collect(Collectors.toList())
        );
    }

    private Optional<PortfolioDTO> toPortfolioDTO(Optional<Portfolio> optionalPortfolio) {
        if (optionalPortfolio.isEmpty())
            return Optional.empty();
        return Optional.of(toPortfolioDTO(optionalPortfolio.get()));
    }

    private Portfolio_Investment_DTO toPortfolioInvestmentDTO(Portfolio portfolio) {
        List<InvestmentDTO> investmentDTOList = new ArrayList<>();
        for (Investment investment:
                portfolio.getInvestments()) {
            investmentDTOList.add(investmentService.toDTO(investment));
        }
        return new Portfolio_Investment_DTO(
                portfolio.getId(),
                portfolio.getOwner(),
                portfolio.getBeginPrice(),
                portfolio.getValue(),
                portfolio.getYield(),
                investmentDTOList
        );
    }


    ////// FIND AS DTO

    public List<PortfolioDTO> findAllAsPortfolioDTO() {
        return portfolioRepository.findAll().stream().map(this::toPortfolioDTO).collect(Collectors.toList());
    }

    public List<Portfolio_Investment_DTO> findAllAsPortfolioInvestmentDTO() {
        return portfolioRepository.findAll().stream().map(this::toPortfolioInvestmentDTO).collect(Collectors.toList());
    }

    public Optional<Portfolio_Investment_DTO> findByIdAsPortfolioInvestmentDTO(Long id) {
        Optional<Portfolio> optionalPortfolio = portfolioRepository.findById(id);
        return optionalPortfolio.map(this::toPortfolioInvestmentDTO);
    }


    ////// FIND

    public Optional<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }

    public Optional<Portfolio> findByOwner(String owner) {
        return portfolioRepository.findByOwner(owner);
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }


    ////// POST, PUT

    @Transactional
    public void saveMyPortfolio() {
        if(this.portfolioRepository.findByOwner("Mikols").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.investmentService.saveMyInvestments(), "Mikolas");
            this.portfolioRepository.save(portfolio);
        }
    }

    @Transactional
    public void save(PortfolioCreateDTO portfolioCreateDTO) {
        List<Product> productList = this.productService.findProducts( portfolioCreateDTO.getInvestmentIds() );
        List<Investment> investmentList = new ArrayList<>();

        for (Product product:productList) {
            investmentList.add(this.investmentService.save(product));
        }
        this.portfolioRepository.save(
                new Portfolio(
                        investmentList,
                        portfolioCreateDTO.getOwner()
                )
        );
        System.out.println(">> Save PortfolioCreateDTO " + portfolioCreateDTO.getOwner());
    }

    @Transactional
    public void save(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
        System.out.println(">> Save Portfolio");
    }

    @Transactional
    public void update(long portfolioId) {
        Optional<Portfolio> optionalPortfolio = portfolioRepository.findById(portfolioId);
        if (optionalPortfolio.isPresent()) {
            Portfolio portfolio = optionalPortfolio.get();
            List<Investment> investmentList = portfolio.getInvestments();
            for (Investment investment : investmentList) {
                investment.setYield();
            }
            portfolio.setValue();
            portfolio.setYield();
            portfolio.setBeginPrice();
        }
    }


}

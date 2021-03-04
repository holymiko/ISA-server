package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.InvestmentDTO;
import home.holymiko.ScrapApp.Server.DTO.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.Portfolio_Investment_DTO;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Investment;
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
    private final InvestmentService investmentService;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, InvestmentService investmentService) {
        this.portfolioRepository = portfolioRepository;
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



    ////// GET

    public Optional<Portfolio> findById(Long id) {
        return portfolioRepository.findById(id);
    }

    public Optional<Portfolio_Investment_DTO> findByIdAsPortfolioInvestmentDTO(Long id) {
        Optional<Portfolio> optionalPortfolio = portfolioRepository.findById(id);
        return optionalPortfolio.map(this::toPortfolioInvestmentDTO);
    }

    public Optional<Portfolio> findByOwner(String owner) {
        return portfolioRepository.findByOwner(owner);
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    public List<PortfolioDTO> findAllAsPortfolioDTO() {
        return portfolioRepository.findAll().stream().map(this::toPortfolioDTO).collect(Collectors.toList());
    }

    public List<Portfolio_Investment_DTO> findAllAsPortfolioInvestmentDTO() {
        return portfolioRepository.findAll().stream().map(this::toPortfolioInvestmentDTO).collect(Collectors.toList());
    }



    ////// POST, PUT

    @Transactional
    public void save(Portfolio portfolio) {
        portfolio.setValue();
        portfolio.setYield();
        portfolio.setBeginPrice();
        this.portfolioRepository.save(portfolio);
        System.out.println("PortfolioDTO saved");
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

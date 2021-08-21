package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.toDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.Portfolio;
import home.holymiko.InvestmentScraperApp.Server.Repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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

    ////// FIND AS DTO

    public List<PortfolioDTO> findAllAsPortfolioDTO(int switcher) {
        return findAllAsPortfolioDTO(findAll(), switcher);
    }

    public List<PortfolioDTO> findAllAsPortfolioDTO(List<Portfolio> portfolioList, int switcher) {
        return switch (switcher) {
            case 0 -> portfolioList
                        .stream()
                        .map(
                                toDTO::toSimpleDTO
                        )
                        .collect(Collectors.toList());
            case 1 -> portfolioList
                        .stream()
                        .map(
                                toDTO::toPortfolioDTO_InvestmentCount
                        )
                        .collect(Collectors.toList());
            case 2 -> portfolioList
                        .stream()
                        .map(
                                toDTO::toDTO_LatestPrices
                        )
                        .collect(Collectors.toList());
            case 3 -> portfolioList
                        .stream()
                        .map(
                                toDTO::toDTO_OneLatestPrice
                        )
                        .collect(Collectors.toList());
            case 4 -> portfolioList
                        .stream()
                        .map(
                                toDTO::toDTO_LatestPrices_OneLatestPrice
                        )
                        .collect(Collectors.toList());
            case 5 -> portfolioList
                        .stream()
                        .map(
                                toDTO::toDTO_AllPrices
                        )
                        .collect(Collectors.toList());
            default -> new ArrayList<>();
        };
    }

    public Optional<PortfolioDTO> findByIdAsPortfolioDTO(Long portfolioId, int dtoSwitch) {
        return portfolioRepository.findById(portfolioId)
                .map(
                        portfolio ->
                                findAllAsPortfolioDTO(
                                        Collections.singletonList(portfolio),
                                        dtoSwitch
                                ).get(0)
                );
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
    public void saveInitPortfolios() {
        if(this.portfolioRepository.findByOwner("Carlos").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.investmentService.saveCarlosInvestments(), "Carlos");
            this.portfolioRepository.save(portfolio);
        }
        if(this.portfolioRepository.findByOwner("Sanchez").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.investmentService.saveSanchezInvestments(), "Sanchez");
            this.portfolioRepository.save(portfolio);
        }
        if(this.portfolioRepository.findByOwner("Eduardo").isEmpty()) {
            Portfolio portfolio = new Portfolio(this.investmentService.saveEduardoInvestments(), "Eduardo");
            this.portfolioRepository.save(portfolio);
        }
    }

//    @Transactional
//    public void save(PortfolioCreateDTO portfolioCreateDTO) {
//        List<Product> productList = this.productService.findProducts( portfolioCreateDTO.getInvestmentIds() );
//        List<InvestmentMetal> investmentMetalList = new ArrayList<>();
//
//        for (Product product:productList) {
//            investmentMetalList.add(this.investmentService.save(product));
//        }
//        this.portfolioRepository.save(
//                new Portfolio(
//                        investmentMetalList,
//                        portfolioCreateDTO.getOwner()
//                )
//        );
//        System.out.println(">> Save PortfolioCreateDTO " + portfolioCreateDTO.getOwner());
//    }

//    @Transactional
//    public void save(Portfolio portfolio) {
//        this.portfolioRepository.save(portfolio);
//        System.out.println(">> Save Portfolio");
//    }
//
//    @Transactional
//    public void save(String name) {
//        this.portfolioRepository.save(new Portfolio(new ArrayList<>(), name));
//        System.out.println(">> Save Portfolio");
//    }

//    @Transactional
//    public List<InvestmentMetal> addInvestmentToPortfolio(String portfolioOwner, InvestmentMetal investmentMetal) {
//        List<InvestmentMetal> investmentMetalList = this.portfolioRepository.findByOwner(portfolioOwner).get().getInvestments();
//        investmentMetalList.add(investmentMetal);
//        return investmentMetalList;
//    }


}

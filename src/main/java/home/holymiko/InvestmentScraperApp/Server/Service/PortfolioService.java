package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Portfolio;
import home.holymiko.InvestmentScraperApp.Server.Mapper.PortfolioMapper;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PortfolioRepository;
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
    private final PortfolioMapper portfolioMapper;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, ProductService productService, PortfolioMapper portfolioMapper) {
        this.portfolioRepository = portfolioRepository;
        this.productService = productService;
        this.portfolioMapper = portfolioMapper;
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
                                portfolioMapper::toPortfolioDTO
                        )
                        .collect(Collectors.toList());
            case 1 -> portfolioList
                        .stream()
                        .map(
                                portfolioMapper::toPortfolioDTO_InvestmentCount
                        )
                        .collect(Collectors.toList());
            case 2 -> portfolioList
                        .stream()
                        .map(
                                portfolioMapper::toDTO_LatestPrices
                        )
                        .collect(Collectors.toList());
            case 3 -> portfolioList
                        .stream()
                        .map(
                                portfolioMapper::toDTO_OneLatestPrice
                        )
                        .collect(Collectors.toList());
            case 4 -> portfolioList
                        .stream()
                        .map(
                                portfolioMapper::toDTO_LatestPrices_OneLatestPrice
                        )
                        .collect(Collectors.toList());
            case 5 -> portfolioList
                        .stream()
                        .map(
                                portfolioMapper::toDTO_AllPrices
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

    public Optional<PortfolioDTO_ProductDTO> findById(Long id) {
        return portfolioRepository.findById(id)
                .map(
                        portfolioMapper::toDTO_AllPrices
                );
    }

    public Optional<PortfolioDTO_ProductDTO> findByOwner(String owner) {
        return portfolioRepository.findByOwner(owner)
                .map(
                        portfolioMapper::toDTO_AllPrices
                );
    }

    public List<PortfolioDTO> findAllAsDTO() {
        return portfolioRepository.findAll()
                .stream()
                .map(
                        portfolioMapper::toPortfolioDTO
                )
                .collect(Collectors.toList());
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }


    ////// POST, PUT



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

    @Transactional
    public void save(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
        System.out.println(">> Save Portfolio");
    }
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

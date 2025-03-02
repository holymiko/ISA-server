package home.holymiko.investment.scraper.app.server.service;

import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.PortfolioDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Portfolio;
import home.holymiko.investment.scraper.app.server.mapper.PortfolioMapper;
import home.holymiko.investment.scraper.app.server.api.repository.PortfolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class PortfolioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioService.class);
    private final PortfolioRepository portfolioRepository;
    private final ProductService productService;
    private final PortfolioMapper portfolioMapper;

    ////// FIND AS DTO

    public List<PortfolioDTO> findAllAsPortfolioDTO(int switcher) {
        return findAllAsPortfolioDTO(portfolioRepository.findAll(), switcher);
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

    public PortfolioDTO_ProductDTO findByIdAsDTO(Long id) {
        return portfolioMapper.toDTO_AllPrices( findById(id) );
    }

    public Optional<PortfolioDTO_ProductDTO> findByOwner(String owner) {
        // TODO Implement
        return Optional.empty();
    }

    public List<PortfolioDTO> findAllAsDTO() {
        return portfolioRepository.findAll()
                .stream()
                .map(
                        portfolioMapper::toPortfolioDTO
                )
                .collect(Collectors.toList());
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
//        LOGGER.info("Save PortfolioCreateDTO " + portfolioCreateDTO.getOwner());
//    }

    @Transactional
    public void save(Portfolio portfolio) {
        this.portfolioRepository.save(portfolio);
        LOGGER.info("Save Portfolio");
    }

    /////////// UTILS
    public Portfolio findById(Long id) {
        Optional<Portfolio> optional = this.portfolioRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Portfolio with ID "+id+" was not found");
        }
        return optional.get();
    }
//
//    @Transactional
//    public void save(String name) {
//        this.portfolioRepository.save(new Portfolio(new ArrayList<>(), name));
//        LOGGER.info("Save Portfolio");
//    }

//    @Transactional
//    public List<InvestmentMetal> addInvestmentToPortfolio(String portfolioOwner, InvestmentMetal investmentMetal) {
//        List<InvestmentMetal> investmentMetalList = this.portfolioRepository.findByOwner(portfolioOwner).get().getInvestments();
//        investmentMetalList.add(investmentMetal);
//        return investmentMetalList;
//    }


}

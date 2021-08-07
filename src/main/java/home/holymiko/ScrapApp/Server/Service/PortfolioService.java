package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.advanced.PortfolioDTO_Investments;
import home.holymiko.ScrapApp.Server.DTO.toDTO;
import home.holymiko.ScrapApp.Server.Entity.InvestmentMetal;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<PortfolioDTO> findAllAsPortfolioDTO() {
        return portfolioRepository.findAll()
                .stream()
                .map(toDTO::toPortfolioDTO)
                .collect(Collectors.toList());
    }

    public List<PortfolioDTO_Investments> findAllAsPortfolioInvestmentDTO() {
        return portfolioRepository.findAll()
                .stream()
                .map(toDTO::toPortfolioInvestmentDTO)
                .collect(Collectors.toList());
    }

    public Optional<PortfolioDTO_Investments> findByIdAsPortfolioInvestmentDTO(Long portfolioId) {
        return portfolioRepository.findById(portfolioId)
                .map(toDTO::toPortfolioInvestmentDTO);
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

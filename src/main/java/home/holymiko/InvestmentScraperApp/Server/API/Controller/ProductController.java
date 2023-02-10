package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/product")         // Na url/api/v1/herbivores se zavola HTTP request
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /////// GET

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<ProductDTO_AllPrices> byId(@PathVariable long id) {
        LOGGER.info("Get product by Id");
        return productService.findByIdAsDTOAllPrices(id);
    }

    /////// GET DTO

    @ResourceNotFound
    @GetMapping("/dto/id/{id}")
    public Optional<ProductDTO_LatestPrices> byIdAsDTO(@PathVariable long id) {
        LOGGER.info("Get by Id "+id+" as Product DTO");
        return productService.findByIdAsDTO(id);
    }

    @GetMapping({ "/dto/", "/dto"})
    public List<ProductDTO_LatestPrices> allAsDTO() {
        LOGGER.info("Get all products as DTO");
        return productService.findAllAsDTO();
    }

    @GetMapping({ "/dto/metal/{metal}", "/dto/metal/{metal}/"})
    public List<ProductDTO_LatestPrices> byMetalAsDTO(@PathVariable String metal) {
        LOGGER.info("Get products byMetal "+metal+" as DTO");
        return switch (metal) {
            case "gold" -> this.productService.findByMetalAsDTO(Metal.GOLD);
            case "silver" -> this.productService.findByMetalAsDTO(Metal.SILVER);
            case "platinum" -> this.productService.findByMetalAsDTO(Metal.PLATINUM);
            case "palladium" -> this.productService.findByMetalAsDTO(Metal.PALLADIUM);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

}

package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Entity.Product;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/product")         // Na url/api/v1/herbivores se zavola HTTP request
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /////// GET

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<ProductDTO_AllPrices> byId(@PathVariable long id) {
        System.out.println("Get product by Id");
        return productService.findByIdAsDTOAllPrices(id);
    }

    @GetMapping({ "/metal/{metal}", "/metal/{metal}/"})
    public List<ProductDTO_LatestPrices> byMetal(@PathVariable String metal) {
        System.out.println("Get products byMetal "+metal);
        return switch (metal) {
            case "gold" -> this.productService.findByMetal(Metal.GOLD);
            case "silver" -> this.productService.findByMetal(Metal.SILVER);
            case "platinum" -> this.productService.findByMetal(Metal.PLATINUM);
            case "palladium" -> this.productService.findByMetal(Metal.PALLADIUM);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

    /////// GET DTO

    @ResourceNotFound
    @GetMapping("/dto/id/{id}")
    public Optional<ProductDTO_LatestPrices> byIdAsDTO(@PathVariable long id) {
        System.out.println("Get by Id "+id+" as Product DTO");
        return productService.findByIdAsDTO(id);
    }

    @GetMapping({ "/dto/", "/dto"})
    public List<ProductDTO_LatestPrices> allAsDTO() {
        System.out.println("Get all products as DTO");
        return productService.findAllAsDTO();
    }

    @GetMapping({ "/dto/metal/{metal}", "/dto/metal/{metal}/"})
    public List<ProductDTO_LatestPrices> byMetalAsDTO(@PathVariable String metal) {
        System.out.println("Get products byMetal "+metal+" as DTO");
        return switch (metal) {
            case "gold" -> this.productService.findByMetalAsDTO(Metal.GOLD);
            case "silver" -> this.productService.findByMetalAsDTO(Metal.SILVER);
            case "platinum" -> this.productService.findByMetalAsDTO(Metal.PLATINUM);
            case "palladium" -> this.productService.findByMetalAsDTO(Metal.PALLADIUM);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

}

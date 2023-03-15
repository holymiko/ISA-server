package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @GetMapping("/id/{id}")
    public ProductDTO_AllPrices byId(@PathVariable long id) {
        System.out.println("Get product by Id");
        return productService.findByIdAsDTOAllPrices(id);
    }

    /////// GET DTO

    @GetMapping("/dto/id/{id}")
    public ProductDTO_LatestPrices byIdAsDTO(@PathVariable long id) {
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
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        };
    }

}

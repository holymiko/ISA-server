package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.DTO.Portfolio_Investment_DTO;
import home.holymiko.ScrapApp.Server.DTO.ProductDTO;
import home.holymiko.ScrapApp.Server.Entity.Metal;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Scrap;
import home.holymiko.ScrapApp.Server.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/product")         // Na url/api/v1/herbivores se zavola HTTP request
public class ProductController {

    private final ProductService productService;
    private final Scrap scrap;

    @Autowired
    public ProductController(ProductService productService, Scrap scrap) {
        this.productService = productService;
        this.scrap = scrap;
    }

    /////// GET

    @GetMapping({ "/dto/", "/dto"})
    public List<ProductDTO> allAsDTO() {
        System.out.println("Get all products as DTO");
        return productService.findAllAsDTO();
    }

    @GetMapping("/id/{id}")
    public Product byId(@PathVariable long id) {
        System.out.println("Get product by Id");
        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dto/id/{id}")
    public ProductDTO byIdAsDTO(@PathVariable long id) {
        System.out.println("Get by Id "+id+" as Product DTO");

        Optional<ProductDTO> optionalPortfolio = productService.findByIdAsDTO(id);
        if (optionalPortfolio.isPresent()) {
            return optionalPortfolio.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping({ "/dto/metal/{metal}", "/dto/metal/{metal}/"})
    public List<ProductDTO> getMetalProductsAsDTO(@PathVariable String metal) {
        System.out.println("Get products byMetal "+metal);
        return switch (metal) {
            case "gold" -> this.productService.findByMetalAsDTO(Metal.GOLD);
            case "silver" -> this.productService.findByMetalAsDTO(Metal.SILVER);
            case "platinum" -> this.productService.findByMetalAsDTO(Metal.PLATINUM);
            case "palladium" -> this.productService.findByMetalAsDTO(Metal.PALLADIUM);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

    public List<Product> getGoldProducts() {
        return this.productService.findByMetal(Metal.GOLD);
    }

//    public List<Product> getGoldProducts(double maxPrice) {
//        return this.productService.findByMetal(maxPrice);
//    }

}

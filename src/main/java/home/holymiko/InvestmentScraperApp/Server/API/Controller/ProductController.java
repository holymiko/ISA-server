package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Handler;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.LinkChangeDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/product")
@AllArgsConstructor
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    // TODO Remove /dto/ from path. We always return DTO. Sync FE

    /////// GET

    @GetMapping("/id/{id}")
    public ProductDTO_AllPrices byId(@PathVariable long id) {
        LOGGER.info("Get product by Id");
        return productService.findByIdAsDTOAllPrices(id);
    }

    /////// GET DTO

    @GetMapping("/dto/id/{id}")
    public ProductDTO_LatestPrices byIdAsDTO(@PathVariable long id) {
        LOGGER.info("Get by Id "+id+" as Product DTO");
        return productService.findByIdAsDTO(id);
    }

    @GetMapping({ "/dto/", "/dto"})
    public List<ProductDTO_LatestPrices> allAsDTO() {
        LOGGER.info("Get all products as DTO");
        return productService.findAllAsDTO();
    }

    // TODO Rebuild to @RequestParam
    @GetMapping({ "/dto/metal/{metal}", "/dto/metal/{metal}/"})
    public List<ProductDTO_LatestPrices> byMetalAsDTO(@PathVariable String metal) {
        LOGGER.info("Get products byMetal "+metal+" as DTO");
        return switch (metal.toLowerCase()) {
            case "gold" -> this.productService.findByMetalAsDTO(Metal.GOLD);
            case "silver" -> this.productService.findByMetalAsDTO(Metal.SILVER);
            case "platinum" -> this.productService.findByMetalAsDTO(Metal.PLATINUM);
            case "palladium" -> this.productService.findByMetalAsDTO(Metal.PALLADIUM);
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only possible values for path variable are: 'gold', 'silver', 'platinum' and 'palladium'");
        };
    }

    /////// PUT

    @PutMapping({ "/link", "/link/"})
    @Operation(description = "Mocked. Removes reference between product (fromProductId) and link. " +
            "When toProductId is given, creates reference between existing product (toProductId) and link. " +
            "When toProductId is missing, creates new product to save the link separately.")
    public void changeLinkProduct(@RequestBody LinkChangeDTO linkChangeDTO) {
        LOGGER.info("changeLinkProduct");
        Assert.notNull(linkChangeDTO, "linkChangeDTO cannot be null");
        Assert.notNull(linkChangeDTO.getLinkId(), "linkId cannot be null");
        Assert.notNull(linkChangeDTO.getFromProductId(), "fromProductId cannot be null");
        this.productService.changeLinkProduct(linkChangeDTO);
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException: " + ex.getMessage());
        Handler.handleIllegalArgumentException(HttpStatus.BAD_REQUEST, ex, response);
    }

}

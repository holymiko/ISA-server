package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Handler;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.LinkChangeDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_Link_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Service.ProductService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v2/product")
@AllArgsConstructor
public class ProductController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    /////// GET

    @GetMapping
    @Operation(description = "Without params, returns all products")
    public List<ProductDTO_LatestPrices> byParams(
            @RequestParam(required = false) Dealer dealer,
            @RequestParam(required = false) Producer producer,
            @RequestParam(required = false) Metal metal,
            @RequestParam(required = false) Form form,
            @RequestParam(required = false) Double grams,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean saveAlone
    ) {
        LOGGER.info("GET List<ProductDTO_LatestPrices> ByParams {} {} {} {} {} {} {}", dealer, producer, metal, form, grams, year, saveAlone);
        return productService.findByParams(dealer, producer, metal, form, grams, year, saveAlone, Pageable.unpaged());
    }

    @GetMapping("/{id}")
    public ProductDTO byId(@PathVariable Long id,
                           @RequestParam(required = false) @Parameter(description = "For dto = 1 returns new DTO") Long dto) {
        LOGGER.info("Get product by ID"+id);
        Assert.notNull(id, "Id cannot be null");
        if(dto == null || dto != 1) {
            return productService.findByIdAsDTOAllPrices(id);
        }
        return productService.findByIdAsDTOLinkAllPrices(id);
    }

    /////// PUT

    @PutMapping("/link")
    @Operation(description = "Removes reference between product (fromProductId) and link. " +
            "When toProductId is given, creates reference between existing product (toProductId) and link. " +
            "When toProductId is missing, creates new product to save the link separately. " +
            "Enables removal of last link and keeping product without references. " +
            "Returns new/updated Product."
    )
    public ProductDTO_Link_AllPrices updateLinkReference(@RequestBody LinkChangeDTO linkChangeDTO) {
        LOGGER.info("updateLinkReference");
        Assert.notNull(linkChangeDTO, "linkChangeDTO cannot be null");
        Assert.notNull(linkChangeDTO.getLinkId(), "linkId cannot be null");
        Assert.notNull(linkChangeDTO.getFromProductId(), "fromProductId cannot be null");
        return this.productService.changeLinkProduct(linkChangeDTO);
    }

    /////// DELETE
    @DeleteMapping
    @Operation(description = "WARNING: Deletes ALL products. Removes reference between product (fromProductId) and link.")
    public void deleteAll() {
        LOGGER.info("Delete all products");
        this.productService.deleteAll();
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException: " + ex.getMessage());
        Handler.handleIllegalArgumentException(HttpStatus.BAD_REQUEST, ex, response);
    }

}

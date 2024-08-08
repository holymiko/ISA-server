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
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v2/product")
@AllArgsConstructor
public class ProductController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    /////// GET

    @GetMapping("/old")
    @Operation(description = "Without params, returns all products")
    public List<ProductDTO_LatestPrices> byParamsOld(
            @RequestParam(required = false) Dealer dealer,
            @RequestParam(required = false) Producer producer,
            @RequestParam(required = false) Metal metal,
            @RequestParam(required = false) Form form,
            @RequestParam(required = false) Double grams,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean savedAlone
    ) {
        LOGGER.info("GET List<ProductDTO_LatestPrices> ByParamsOld {} {} {} {} {} {} {}", dealer, producer, metal, form, grams, year, savedAlone);
        return productService.findByParamsOld(dealer, producer, metal, form, grams, year, false, Pageable.unpaged());
    }

    @GetMapping
    @Operation(description = "Without params, returns all products")
    public Page<ProductDTO_LatestPrices> byParams(
            @RequestParam(required = false) Dealer dealer,
            @RequestParam(required = false) Producer producer,
            @RequestParam(required = false) Metal metal,
            @RequestParam(required = false) Form form,
            @RequestParam(required = false) Double grams,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Boolean hidden,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Integer size
    ) {
        LOGGER.info("GET List<ProductDTO_LatestPrices> ByParams {} {} {} {} {} {} {}", dealer, producer, metal, form, grams, year, hidden);

        if (size == null) {
            size = Integer.MAX_VALUE;
        }

        // TODO Add sorting, add column bestSpread and price/gram
        return productService.findByParams(dealer, producer, metal, form, grams, year, hidden, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ProductDTO byId(
            @PathVariable Long id,
            @RequestParam(required = false) @Parameter(description = "For dto = 1 returns new DTO") Long dto
    ) {
        LOGGER.info("Get product by ID " + id);
        Assert.notNull(id, "Id cannot be null");
        if(dto == null || dto != 1) {
            return productService.findByIdAsDTOAllPrices(id);
        }
        return productService.findByIdAsDTOLinkAllPrices(id);
    }

    @GetMapping("/exists/{id}")
    public boolean existsById(@PathVariable Long id) {
        LOGGER.info("Exists product by ID " + id);
        Assert.notNull(id, "Id cannot be null");
        return productService.existsById(id);
    }

    /////// PUT

    @PutMapping("/link")
    @Operation(description = "Removes reference between product (fromProductId) and link. " +
            "When toProductId is given, creates reference between existing product (toProductId) and link. " +
            "When toProductId is missing, creates new product to save the link separately. " +
            "Product without Link reference will be automatically removed. " +
            "Returns new/updated Product."
    )
    public ProductDTO_Link_AllPrices updateLinkReference(@RequestBody LinkChangeDTO linkChangeDTO) {
        LOGGER.info("updateLinkReference");
        Assert.notNull(linkChangeDTO, "linkChangeDTO cannot be null");
        Assert.notNull(linkChangeDTO.getLinkId(), "linkId cannot be null");
        Assert.notNull(linkChangeDTO.getFromProductId(), "fromProductId cannot be null");
        return this.productService.updateLinkReference(linkChangeDTO);
    }

    /////// DELETE
    @DeleteMapping("/{id}")
    @Operation(description = "WARNING: Deletes product. Removes reference between product (fromProductId) and link.")
    public void deleteById(@PathVariable Long id) {
        LOGGER.info("Delete product by ID "+id);
        this.productService.deleteById(id);
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException: " + ex.getMessage());
        Handler.handleIllegalArgumentException(HttpStatus.BAD_REQUEST, ex, response);
    }

}

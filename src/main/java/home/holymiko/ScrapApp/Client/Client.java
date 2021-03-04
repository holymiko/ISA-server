package home.holymiko.ScrapApp.Client;

import home.holymiko.ScrapApp.Server.Controller.LinkController;
import home.holymiko.ScrapApp.Server.Controller.PortfolioController;
import home.holymiko.ScrapApp.Server.Controller.ProductController;
import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Scrap;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class Client {
    private final ProductController productController;
    private final PortfolioController portfolioController;
    private final LinkController linkController;

    @Autowired
    public Client(ProductController productController, PortfolioController portfolioController, LinkController linkController) {
        this.productController = productController;
        this.portfolioController = portfolioController;
        this.linkController = linkController;
    }

    public void run() {
        System.out.println(">>>>> RUN <<<<<");
//        this.linkController.saveAllLinks();
//        this.productController.saveAll();
//        this.portfolioController.saveMyPortfolio();
//        this.portfolioController.updatePortfolioProducts();
//        printProducts(this.productController.getGoldProducts(20000));
//        printMyPortfolio( this.portfolioController.byOwner("Mikolas"));
    }


}

package home.holymiko.ScrapApp;

import home.holymiko.ScrapApp.Client.Client;
import home.holymiko.ScrapApp.Server.Controller.LinkController;
import home.holymiko.ScrapApp.Server.Controller.PortfolioController;
import home.holymiko.ScrapApp.Server.Controller.ProductController;
import home.holymiko.ScrapApp.Server.Scrap;
import home.holymiko.ScrapApp.Server.Service.InvestmentService;
import home.holymiko.ScrapApp.Server.Service.PortfolioService;
import home.holymiko.ScrapApp.Server.Service.ProductService;
import home.holymiko.ScrapApp.Server.Service.PriceService;
import home.holymiko.ScrapApp.Server.Service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.swing.text.html.HTMLEditorKit;
import java.util.Collections;

@SpringBootApplication
public class ScrapApp {

//    @Autowired
//    private ProductController productController;
//    @Autowired
//    private PortfolioController portfolioController;
//    @Autowired
//    private LinkController linkController;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ScrapApp.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));
        app.run();
    }
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void run() {
//        Client client = new Client(this.productController, this.portfolioController, this.linkController);
//        client.run();
//    }
}

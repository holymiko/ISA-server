package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.Entity.Investment;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Price;
import home.holymiko.ScrapApp.Server.Entity.Product;

import java.util.List;

public class Utils {

    public static void printProducts(List<Product> products) {
        if (!products.isEmpty()) {
            System.out.println(">>>>>>> Products <<<<<<<");
            System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Price", "Price / Gram", "Spread");
            System.out.println( "----------------------------------------------------------------------------");
            for (Product product : products) {
                Price latestPrice = product.getLatestPrice();
                if(latestPrice != null) {
                    System.out.printf("          %-90s%-10.2f   %10.2f    %-15.2f  %-10f\n",
                            product.getName(),
                            product.getGrams(),
                            product.getLatestPrice().getPrice(),
                            product.getLatestPrice().getPrice() / product.getGrams(),
                            product.getLatestPrice().getRedemption() / product.getLatestPrice().getPrice()
                    );
                } else {
                    System.out.printf("          %-90s%-10.2f   %10.2f    %-15.2f  %-10f\n",
                            product.getName(),
                            product.getGrams(),
                            0.0,
                            0.0,
                            0.0
                    );
                }
            }
        } else
            System.out.println("No products here");
    }

    public static void printPortfolio(Portfolio portfolio) {
        List<Investment> investmentList = portfolio.getInvestments();
        System.out.println(">>>>>>> PortfolioDTO <<<<<<<<\n");
        System.out.println( "Owner: " + portfolio.getOwner()+"\n");
        System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Nakup", "Vykup", "Zhodnoceni");
        System.out.println( "----------------------------------------------------------------------------\n");
        for (Investment investment : investmentList) {
            System.out.printf( "%1$-120s", investment.getProduct().getName());
            System.out.printf( "%-15.2f%-10.2f%-15s%-10s\n",
                    investment.getProduct().getGrams(),
                    investment.getBeginPrice(),
                    investment.getProduct().getLatestPrice().getRedemption(),
                    investment.getTextYield()
            );
        }
        System.out.println( "----------------------------------------------------------------------------\n");
        System.out.printf( "%103.0f%10.0f%13s\n", portfolio.getBeginPrice(), portfolio.getValue(), portfolio.getTextYield());
    }
}

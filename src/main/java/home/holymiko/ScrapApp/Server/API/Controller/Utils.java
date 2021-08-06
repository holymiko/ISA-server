package home.holymiko.ScrapApp.Server.API.Controller;

import home.holymiko.ScrapApp.Server.Entity.InvestmentMetal;
import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import home.holymiko.ScrapApp.Server.Entity.Price;
import home.holymiko.ScrapApp.Server.Entity.Product;

import java.util.List;

@Deprecated
public class Utils {

    public static void printProducts(List<Product> products) {
        if (!products.isEmpty()) {
            System.out.println(">>>>>>> Products <<<<<<<");
            System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Price", "Price / Gram", "Spread");
            System.out.println( "----------------------------------------------------------------------------");
            for (Product product : products) {
                List<Price> latestPrice = product.getLatestPrices();
                if(latestPrice != null) {
                    System.out.printf("          %-90s%-10.2f   %10.2f    %-15.2f  %-10f\n",
                            product.getName(),
                            product.getGrams(),
                            product.getLatestPrices().get(0).getPrice(),
                            product.getLatestPrices().get(0).getPrice() / product.getGrams(),
                            product.getLatestPrices().get(0).getRedemption() / product.getLatestPrices().get(0).getPrice()
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
        List<InvestmentMetal> investmentList = portfolio.getInvestmentMetals();
        System.out.println(">>>>>>> PortfolioDTO <<<<<<<<\n");
        System.out.println( "Owner: " + portfolio.getOwner()+"\n");
        System.out.printf( "          %-90s%-15s%-10s%-15s\n", "Nazev", "Gramy", "Nakup", "Vykup");
        System.out.println( "----------------------------------------------------------------------------\n");
        for (InvestmentMetal investment : investmentList) {
            System.out.printf( "%1$-120s", investment.getProduct().getName());
            System.out.printf( "%-15.2f%-10.2f%-15s\n",
                    investment.getProduct().getGrams(),
                    investment.getBeginPrice(),
                    investment.getProduct().getLatestPriceByDealer(investment.getDealer()).getRedemption()
            );
        }
        System.out.println( "----------------------------------------------------------------------------\n");
    }

    public String getTextYield(Double yield) {
        if (yield >= 1)
            return "+" + String.format("%.2f", (yield - 1) * 100) + "%";
        return "-" + String.format("%.2f", (100 - yield * 100)) + "%";
    }
}

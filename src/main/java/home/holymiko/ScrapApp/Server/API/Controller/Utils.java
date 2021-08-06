package home.holymiko.ScrapApp.Server.API.Controller;

@Deprecated
public class Utils {

//    public static void printProducts(List<Product> products) {
//        if (!products.isEmpty()) {
//            System.out.println(">>>>>>> Products <<<<<<<");
//            System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Price", "Price / Gram", "Spread");
//            System.out.println( "----------------------------------------------------------------------------");
//            for (Product product : products) {
//                Price latestPrice = product.getLatestPrices();
//                if(latestPrice != null) {
//                    System.out.printf("          %-90s%-10.2f   %10.2f    %-15.2f  %-10f\n",
//                            product.getName(),
//                            product.getGrams(),
//                            product.getLatestPrices().getPrice(),
//                            product.getLatestPrices().getPrice() / product.getGrams(),
//                            product.getLatestPrices().getRedemption() / product.getLatestPrices().getPrice()
//                    );
//                } else {
//                    System.out.printf("          %-90s%-10.2f   %10.2f    %-15.2f  %-10f\n",
//                            product.getName(),
//                            product.getGrams(),
//                            0.0,
//                            0.0,
//                            0.0
//                    );
//                }
//            }
//        } else
//            System.out.println("No products here");
//    }

//    public static void printPortfolio(Portfolio portfolio) {
//        List<InvestmentMetal> investmentList = portfolio.getInvestments();
//        System.out.println(">>>>>>> PortfolioDTO <<<<<<<<\n");
//        System.out.println( "Owner: " + portfolio.getOwner()+"\n");
//        System.out.printf( "          %-90s%-15s%-10s%-15s%-10s\n", "Nazev", "Gramy", "Nakup", "Vykup", "Zhodnoceni");
//        System.out.println( "----------------------------------------------------------------------------\n");
//        for (InvestmentMetal investment : investmentList) {
//            System.out.printf( "%1$-120s", investment.getProduct().getName());
//            System.out.printf( "%-15.2f%-10.2f%-15s%-10s\n",
//                    investment.getProduct().getGrams(),
//                    investment.getBeginPrice(),
//                    investment.getProduct().getLatestPrices().getRedemption(),
//                    investment.getTextYield()
//            );
//        }
//        System.out.println( "----------------------------------------------------------------------------\n");
//        System.out.printf( "%103.0f%10.0f%13s\n", portfolio.getBeginPrice(), portfolio.getValue(), portfolio.getTextYield());
//    }
//
//    public String getTextYield(Double yield) {
//        if (yield >= 1)
//            return "+" + String.format("%.2f", (yield - 1) * 100) + "%";
//        return "-" + String.format("%.2f", (100 - yield * 100)) + "%";
//    }
}

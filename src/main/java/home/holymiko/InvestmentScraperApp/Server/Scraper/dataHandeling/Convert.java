package home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.*;

import java.util.Locale;

public class Convert {

    /**
     * Converts text to
     * @param nameOfDealer including name of form
     * @return Enum class Form
     */
    public static Dealer dealerConvert(String nameOfDealer) throws IllegalArgumentException {
        return switch (nameOfDealer.toLowerCase(Locale.ROOT)) {
            case "bessergold" -> Dealer.BESSERGOLD_CZ;
            case "zlataky" -> Dealer.ZLATAKY;
            default -> throw new IllegalArgumentException("Invalid Enum argument");
        };
    }

    /**
     * Extracts number from text format.
     * @param text text including only number and currency
     * @return price from text
     */
    public static Double currencyToNumberConvert(String text) {
        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.replace(" ", "");
        text = text.replace(",", ".");             // -> Double
        text = text.replace("Kč", "");
        text = text.replace("%", "");
        return Double.parseDouble(text);
    }

    /**
     * Applies specifications of class
     * @param text text including only number and currency
     * @return price from text
     */
    public static Double numberConvertSerenity(String text) {
        text = text.replace(",", "");             // -> Double
        return currencyToNumberConvert(text);
    }

    public static GrahamGrade gradeConvert(final String text) throws IllegalArgumentException {
        return switch (text.toLowerCase(Locale.ROOT)) {
            case "enterprising" -> GrahamGrade.ENTERPRISING;
            case "defensive" -> GrahamGrade.DEFENSIVE;
            case "ungraded" -> GrahamGrade.UNGRADED;
            case "ncav" -> GrahamGrade.NCAV;
            default -> throw new IllegalArgumentException("Invalid Enum argument");
        };
    }

    /**
     * Converts String to Metal Enum class.
     * Used case switch
     * @param nameOfMetal - precise name of the metal in Czech or English. Input is CaseInsensitive
     * @return Enum
     * @throws IllegalArgumentException - No Enum was found
     */
    public static Metal metalConvert(String nameOfMetal) throws IllegalArgumentException {
        return switch (nameOfMetal.toLowerCase(Locale.ROOT)) {
            case "zlato", "gold" -> Metal.GOLD;
            case "stříbro", "silver" -> Metal.SILVER;
            case "platina", "platinum" -> Metal.PLATINUM;
            case "palladium" -> Metal.PALLADIUM;
            default -> throw new IllegalArgumentException("Invalid Enum argument");
        };
    }

}

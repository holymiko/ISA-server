package home.holymiko.InvestmentScraperApp.Server.Scraper.extractor;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.*;

import java.util.Locale;

public class Convert {

    public static String currencyClean(String text) throws NullPointerException {
        if(text == null) throw new NullPointerException();
        text = text.toLowerCase();
        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.replace(" ", "");
        text = text.replace(",", ".");             // -> Double
        text = text.replace("kč", "");
        text = text.replace("€", "");
        text = text.replace("$", "");
        text = text.replace("%", "");
        text = text.replace("£", "");
        text = text.replace("¥", "");
        return text;
    }

    /**
     * Extracts number from price text. Preforms currency clean
     * @param text text including only number and currency
     * @throws NumberFormatException empty
     * @throws NullPointerException
     * @return price from text
     */
    public static Double currencyToDouble(String text) throws NullPointerException, NumberFormatException {
        return Double.parseDouble(
                currencyClean(text)
        );
    }

    /**
     *
     * @param text from which number gonna be extracted
     * @param exchangeRate use exchangeRateService
     * @param currencySignature will try to match on text param
     * @return
     * @throws NullPointerException
     */
    public static Double currency(String text, Double exchangeRate, String currencySignature) throws NullPointerException, NumberFormatException, IllegalArgumentException {
        if(text == null || currencySignature == null || exchangeRate == null) {
            throw new NullPointerException("Null pointer in currencyConvert");
        }
        if(currencySignature.equals("")) throw new IllegalArgumentException("Currency signature is empty");
        // Try to find given currency and perform conversion
        if(text.toLowerCase().contains( currencySignature.toLowerCase() )) {
            return currencyToDouble(text) * exchangeRate;
        }
        return currencyToDouble(text);
    }

    /**
     * Applies specifications of class
     * @param text text including only number and currency
     * @return price from text
     */
    public static Double serenityToNumber(String text) throws NullPointerException, NumberFormatException {
        if(text == null) throw new NullPointerException();
        text = text.replace(",", "");             // -> Double
        return currencyToDouble(text);
    }

    /**
     * Converts String to GrahamGrade Enum class.
     * @param nameOfGG
     * @return Enum
     * @throws IllegalArgumentException - No Enum was found
     * @throws NullPointerException
     */
    public static GrahamGrade grahamGrade(final String nameOfGG) throws IllegalArgumentException, NullPointerException {
        if(nameOfGG == null) throw new NullPointerException();
        return switch (nameOfGG.toLowerCase(Locale.ROOT)) {
            case "enterprising" -> GrahamGrade.ENTERPRISING;
            case "defensive" -> GrahamGrade.DEFENSIVE;
            case "ungraded" -> GrahamGrade.UNGRADED;
            case "ncav (net-net)" -> GrahamGrade.NCAV;
            default -> throw new IllegalArgumentException("Invalid Enum argument - '"+nameOfGG+"'");
        };
    }

    /**
     * Converts String to Metal Enum class.
     * Works for Czech, English and German language
     * @param nameOfMetal - precise name of the metal in Czech or English. Input is CaseInsensitive
     * @return Enum
     * @throws IllegalArgumentException - No Enum was found
     * @throws NullPointerException
     */
    public static Metal metal(String nameOfMetal) throws IllegalArgumentException, NullPointerException {
        if(nameOfMetal == null) throw new NullPointerException();
        return switch (nameOfMetal.toLowerCase(Locale.ROOT)) {
            case "zlato", "gold" -> Metal.GOLD;
            case "stříbro", "silver", "silber" -> Metal.SILVER;
            case "platina", "platinum", "platin" -> Metal.PLATINUM;
            case "palladium" -> Metal.PALLADIUM;
            default -> throw new IllegalArgumentException("Invalid Enum argument");
        };
    }

}

package home.holymiko.ScrapApp.Server.Scraps;

import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {

    private static final double TROY_OUNCE = 31.1034768;
    private static final double OUNCE = 28.349523125;

    /**
     * Extracts Producer from text
     * @param text including producer's name
     * @return Enum class Producer
     */
    public static Producer producerExtractor(String text) {
        text = text.toLowerCase();
        if (text.contains("perth") || text.contains("rok") || text.contains("kangaroo") || text.contains("kookaburra") || text.contains("koala")) {
            return Producer.PERTH_MINT;
        } else if(text.contains("argor")) {
            return Producer.ARGOR_HERAEUS;
        } else if (text.contains("heraeus")) {
            return Producer.HERAEUS;
        } else if (text.contains("münze") || text.contains("wiener philharmoniker")) {
            return Producer.MUNZE_OSTERREICH;
        } else if (text.contains("rand") || text.contains("krugerrand")) {
            return Producer.SOUTH_AFRICAN_MINT;
        } else if (text.contains("pamp")) {
            return Producer.PAMP;
        } else if (text.contains("valcambi")) {
            return Producer.VALCAMBI;
        } else if (text.contains("royal canadian mint") || text.contains("maple leaf") || text.contains("moose") || text.contains("golden eagle")) {
            return Producer.ROYAL_CANADIAN_MINT;
        } else if (text.contains("panda čína")) {
            return Producer.CHINA_MINT;
        } else if (text.contains("american eagle")) {
            return Producer.UNITED_STATES_MINT;
        } else if (text.contains("britannia") || text.contains("sovereign elizabeth")) {
            return Producer.ROYAL_MINT_UK;
        } else if (text.contains("libertad") || text.contains("mexico") || text.contains("mexiko")) {
            return Producer.MEXICO_MINT;
        } else if (text.contains("slon") ) {
            return Producer.BAVARIAN_STATE_MINT;
        } else if (text.contains("noble isle of man") ) {
            return Producer.POBJOY_MINT;
        } else if (text.contains("kanada")){
            return Producer.ROYAL_CANADIAN_MINT;
        } else if (text.contains("austrálie")) {
            return Producer.PERTH_MINT;
        } else if (text.contains("usa") || text.contains("american")) {
            return Producer.UNITED_STATES_MINT;
        }

        return Producer.UNKNOWN;
    }

    /**
     * Extracts Form from text
     * @param text including name of form
     * @return Enum class Form
     */
    public static Form formExtractor(String text) {
        text = text.toLowerCase();
        if(text.contains("mince") || text.contains("coin") || text.contains("tolar")){
            return Form.COIN;
        }
        if(text.contains("bar") || text.contains("slitek")){
            return Form.BAR;
        }
        return Form.UNKNOWN;
    }

    /**
     * Extracts weight from various patterns
     * @param text including number with unit
     * @return Grams
     */
    public static double weightExtractor(String text) {
//        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.toLowerCase(Locale.ROOT);

        Pattern pattern = Pattern.compile("\\d+x\\d+g");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Integer.parseInt(s.split("x")[0]) * Integer.parseInt(s.split("x")[1]);
        }

        pattern = Pattern.compile("\\d+\\.\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+,\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            s = s.replace(",", ".");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+ g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+\\/\\d+ oz");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" oz", "");
            return Double.parseDouble(s.split("/")[0]) / Double.parseDouble(s.split("/")[1]) * TROY_OUNCE;
        }

        pattern = Pattern.compile("\\d+ oz");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" oz", "");
            return Double.parseDouble(s) * TROY_OUNCE;
        }

        pattern = Pattern.compile("\\d+ kg");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" kg", "");
            return Double.parseDouble(s) * 1000;
        }

        return -1;
    }


    /**
     * Extracts price from text format.
     * @param text text including only number and currency
     * @return price from text
     */
    public static Double priceExtractor(String text) {
        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.replace(",", ".");             // -> Double
        text = text.replace("Kč", "");
        return Double.parseDouble( text.replace(" ", "") );
    }
}

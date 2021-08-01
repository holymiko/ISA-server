package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.GrahamGrade;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {

    private static final double TROY_OUNCE = 31.1034768;
    private static final double OUNCE = 28.349523125;
    private static final double TOLAR = 28.07;

    /**
     * Extracts Producer from text
     * @param text including producer's name
     * @return Enum class Producer
     */
    public static Producer producerExtractor(String text) {
        text = text.toLowerCase();

        if (text.contains("perth") || text.contains("rok") || text.contains("kangaroo")
        || text.contains("kookaburra") || text.contains("koala") || text.contains("austrálie")
        || text.contains("austral") || text.contains(" emu ") || text.contains("drak a ")
        || text.contains("next generation") || text.contains("black flag") || text.contains("orel klínoocasý") ) {
            return Producer.PERTH_MINT;
        }
        else if(text.contains("argor")) {
            return Producer.ARGOR_HERAEUS;
        }
        else if (text.contains("heraeus")) {
            return Producer.HERAEUS;
        }
        else if (text.contains("münze") || text.contains("wiener philharmoniker")) {
            return Producer.MUNZE_OSTERREICH;
        }
        else if (text.contains("rand") || text.contains("krugerrand")) {
            return Producer.SOUTH_AFRICAN_MINT;
        }
        else if (text.contains("pamp")) {
            return Producer.PAMP;
        }
        else if (text.contains("valcambi")) {
            return Producer.VALCAMBI;
        }
        else if (text.contains("royal canadian mint") || text.contains("maple leaf")
        || text.contains("moose") || text.contains("golden eagle") || text.contains("kanada")
        || text.contains("roaring grizzly") || text.contains("growling cougar") || text.contains("howling wolf")
        || text.contains("zlatá horečka na klondiku") || text.contains(" elk ")){
            return Producer.ROYAL_CANADIAN_MINT;
        }
        else if (text.contains("britannia") || text.contains("sovereign elizabeth")
        || text.contains("the queen´s beasts") || text.contains("the royal mint")
        || text.contains("royal arms") || text.contains("dva draci")
        || text.contains("mýty a legendy") || text.contains("valiant")) {
            return Producer.ROYAL_MINT_UK;
        }
        else if (text.contains("libertad") || text.contains("mexico") || text.contains("mexiko")) {
            return Producer.MEXICO_MINT;
        }
        else if (text.contains("slon") || text.contains("leopard somálsko")) {
            return Producer.BAVARIAN_STATE_MINT;
        }
        else if (text.contains("noble isle of man") ) {
            return Producer.POBJOY_MINT;
        }
        else if (text.contains("usa") || text.contains("american") || text.contains("american eagle")) {
            return Producer.UNITED_STATES_MINT;
        }
        else if (text.contains("panda")) {
            return Producer.CHINA_MINT;
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
        if(text.contains("combibar") || text.contains("multidisc") || text.contains("multigram")) {
            return Form.COMBIBAR;
        }
        if(text.contains("roundbar")) {
            return Form.ROUNDBAR;
        }
        if(text.contains("paket") || text.contains("odběr 100 ks a více")) {
            return Form.PACKET;
        }
        if(text.contains("mince") || text.contains("coin") || text.contains("tolar")) {
            return Form.COIN;
        }
        if(text.contains("kinebar")) {
            return Form.KINEBAR;
        }
        if(text.contains("bar") || text.contains("slitek")) {
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

        pattern = Pattern.compile("\\d+ x \\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Integer.parseInt(s.split(" x ")[0]) * Integer.parseInt(s.split(" x ")[1]);
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

        if (text.contains("tolar") || text.contains("taler")) {
            return TOLAR;
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

    public static Double serenityElementToDouble(final HtmlElement element) {
        String result = element.asText();
        result = result.replace("%", "");
        result = result.replace(",", "");
        return Double.parseDouble(result);
    }

    public static GrahamGrade gradeExtractor(final HtmlElement element) {
        switch (element.asText().toLowerCase(Locale.ROOT)) {
            case "enterprising" -> { return GrahamGrade.ENTERPRISING; }
            case "defensive" -> { return GrahamGrade.DEFENSIVE; }
            case "ungraded" -> { return GrahamGrade.UNGRADED; }
            case "ncav" -> { return GrahamGrade.NCAV; }
            default -> { return GrahamGrade.UNKNOWN; }
        }
    }

    /**
     * Name is converted to lower case.
     * @param name of Product
     * @return metal of product
     */
    public static Metal metalExtractor(String name) {
        name = name.toLowerCase(Locale.ROOT);
        if (name.contains("zlat")) {
            return Metal.GOLD;
        } else if (name.contains("stříbr")) {
            return Metal.SILVER;
        } else if (name.contains("platin")) {
            return Metal.PLATINUM;
        } else if (name.contains("pallad")) {
            return Metal.PALLADIUM;
        } else {
            return Metal.UNKNOWN;
        }
    }
}
